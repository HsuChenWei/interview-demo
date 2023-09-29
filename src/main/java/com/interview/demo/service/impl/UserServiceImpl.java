package com.interview.demo.service.impl;

import com.interview.demo.entity.QUser;
import com.interview.demo.entity.User;
import com.interview.demo.entity.UserRole;
import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.Security.TokenPair;
import com.interview.demo.model.User.UserCreate;
import com.interview.demo.model.User.UserList;
import com.interview.demo.model.User.UserLogin;
import com.interview.demo.model.User.UserUpdate;
import com.interview.demo.repository.UserRepository;
import com.interview.demo.repository.UserRoleRepository;
import com.interview.demo.repository.querydsl.QuerydslRepository;
import com.interview.demo.service.UserService;
import com.interview.demo.service.utils.UtilService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.interview.demo.entity.QUser.user;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private QuerydslRepository queryCtx;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UtilService utilService;

    @Autowired
    private UserService userService;


    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public List<UserList> getAllUserWithRole() {
        List<User> users = userRepository.findAll();
        List<UserRole> userRoles = userRoleRepository.findAll();

        Map<String, Integer> userIdToUserTypeMap = new HashMap<>();
        for (UserRole userRole : userRoles) {
            if (userRole.getUser() != null) {
                userIdToUserTypeMap.put(userRole.getUser().getId(), userRole.getUserType());
            }
        }

        List<UserList> userDTOs = users.stream().map(user -> {
            UserList dto = new UserList();
            dto.setId(user.getId());
            dto.setUserName(user.getUserName());
            dto.setUserType(userIdToUserTypeMap.getOrDefault(user.getId(), null));
            return dto;
        }).collect(Collectors.toList());

        return userDTOs;
    }

    @Override
    public Option<User> getUserById(String id) {
        QUser user = QUser.user;
        return Option.of(queryCtx.newQuery()
                .selectFrom(user)
                .where(user.id.eq(id))
                .fetchOne());
    }

    @Override
    public Option<UserList> getUserByIdWithRole(String id) {
        //1.我需要從user拿到id若沒找到則返回一個空值
        //2.用if來判斷是否有拿到userId
        //3.如果User存在則查詢userRole
        //4.新增一個userList的物件把直塞進userList
        //5.最後判斷是否userRole存在若存在顯示出userId,userName,userType
        //6.若userRole不存在則只返回userId,userName
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            UserRole userRole = userRoleRepository.findById(id).orElse(null);
            UserList userDetail = new UserList();
            userDetail.setId(user.getId());
            userDetail.setUserName(user.getUserName());
            if (userRole != null) {
                userDetail.setUserType(userRole.getUserType());
            }
            return Option.of(userDetail);
        }
        return Option.none();
    }

    //註冊
    @Override
    @Transactional
    public Option<User> createUser(UserCreate creation) {

        //檢查帳號是否被重複創建
        if (userRepository.existsByUserName(creation.getUserName())) {
            throw new BadRequestException(ApiErrorCode.USER_EXISTED);
        }
        //創建帳號
        User user = new User();
        //設定帳號
        user.setUserName(creation.getUserName());
        //把創建的新密碼加密再設定
        String encryptedPassword = passwordEncoder.encode(creation.getUserPwd());
        user.setUserPwd(encryptedPassword);

        User newUser = userRepository.save(user);
        //同時新增使用者角色
        UserRole userRole = new UserRole();
        //設定與userId相同的編號
        userRole.setId(newUser.getId());
        //設定userType=0為初始值(一般會員)
        userRole.setUserType(0);
        userRoleRepository.save(userRole);
        return Option.of(newUser);
    }

    //取得會員帳號
    @Override
    public Option<User> getUserByName(String userName) {
        QUser username = user;
        return Option.of(queryCtx.newQuery()
                .selectFrom(username)
                .where(username.userName.eq(userName))
                .fetchOne());
    }

    //會員登入
    @Override
    public Option<TokenPair> userLogin(UserLogin body) {
        //1.在其台輸入其帳號
        //2.if判斷輸入帳號是否為空值
        //3.如果有值宣告出一個使用者把輸入的值取出
        //4.if判斷調用checkPassword方法是否輸入與資料庫密碼相同
        //5.如果相同簽發一個accessToken,如果不相同則回傳一個錯誤訊息
        //取得帳號前台輸入帳號
        Option<User> userOption = getUserByName(body.getUserName());
        if (userOption.isDefined()) {
            User user = userOption.get();
            //調用檢查DB的加密密碼方法
            if (checkPassword(body, user)) {
                //回傳accessToken
                return utilService.generateTokenPair(user.getId());
            } else {
                throw new BadRequestException(ApiErrorCode.USER_NOT_FOUND);
            }
        }
        return Option.none();
    }

    public List<String> getRoleTypeByUserId(String userId) {
        Option<User> user = userService.getUserById(userId);
        if (user.isDefined()) {
            String roleType = userRoleRepository.findRoleTypeByUserId(userId);
            return Collections.singletonList(roleType);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Option<User> updateUserDetailByUserName(String userName, UserUpdate theUser) {
        Option<User> existingUser = userService.getUserByName(userName);
        if (!existingUser.isDefined()){
            return Option.none();
        }
        User userUpdate = existingUser.get();
        String encoderPwd = passwordEncoder.encode(theUser.getUserPwd());
        userUpdate.setUserPwd(encoderPwd);

        User newUserUpdate = userRepository.save(userUpdate);
        return Option.of(newUserUpdate);
    }


    //檢查DB的加密密碼
    public boolean checkPassword(UserLogin user, User dbUser) {
        //1.前台輸入的密碼與資料庫的密碼進行比對(UserLogin user, User dbUser)
        //2.宣告一個從前台輸入拿到的Password為inputPassword
        //3.回傳一個比對的值為true or false
        String inputPassword = user.getUserPwd();
        return passwordEncoder.matches(inputPassword, dbUser.getUserPwd());
    }


}
