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
import com.interview.demo.repository.UserRepository;
import com.interview.demo.repository.UserRoleRepository;
import com.interview.demo.repository.querydsl.QuerydslRepository;
import com.interview.demo.service.UserService;
import com.interview.demo.service.utils.UtilService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.util.*;
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

        user.setUserName(creation.getUserName());

        //密碼加密
        String encryptedPassword = passwordEncoder.encode(creation.getUserPwd());
        user.setUserPwd(encryptedPassword);

        User newUser = userRepository.save(user);
        //同時新增使用者角色
        UserRole userRole = new UserRole();
        userRole.setId(newUser.getId());//設定與userId相同的roleId
        userRole.setUserType(0);//初始設定值為0(一般用戶)
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
        //取得帳號前台輸入帳號
        Option<User> userOption = getUserByName(body.getUserName());
        if (userOption.isDefined()) {
            User user = userOption.get();
            //調用檢查DB的加密密碼方法
            if (checkPassword(body, user)) {
                //回傳accessToken
                return utilService.generateTokenPair(user.getId());
            }else {
                throw new BadRequestException(ApiErrorCode.USER_NOT_FOUND);
            }
        }
        return Option.none();
    }

    public List<String> getRoleTypeByUserId(String userId) {
        Optional<User> userOptional = userService.getUserById(userId).toJavaOptional();
        if (userOptional.isPresent()) {
            String roleType = userRoleRepository.findRoleTypeByUserId(userId);

            return Collections.singletonList(roleType);
        } else {
            return Collections.emptyList();
        }
    }

    //檢查DB的加密密碼
    public boolean checkPassword(UserLogin user, User dbUser) {
        String inputPassword = user.getUserPwd();
        return passwordEncoder.matches(inputPassword, dbUser.getUserPwd());
    }


}
