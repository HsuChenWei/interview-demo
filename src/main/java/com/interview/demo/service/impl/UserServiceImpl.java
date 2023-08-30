package com.interview.demo.service.impl;

import com.interview.demo.entity.QUser;
import com.interview.demo.entity.User;
import com.interview.demo.entity.UserRole;
import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.User.UserCreate;
import com.interview.demo.model.User.UserLogin;
import com.interview.demo.repository.UserRepository;
import com.interview.demo.repository.UserRoleRepository;
import com.interview.demo.repository.querydsl.QuerydslRepository;
import com.interview.demo.service.UserService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.jdo.annotations.Transactional;
import java.util.List;


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


    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Option<User> getUserById(String id) {
        QUser user = QUser.user;
        return Option.of(queryCtx.newQuery()
                .selectFrom(user)
                .where(user.id.eq(id))
                .fetchOne());
    }

    //取得會員帳號
    @Override
    public Option<User> getUserByName(String userName) {
        QUser username = QUser.user;
        return Option.of(queryCtx.newQuery()
                .selectFrom(username)
                .where(username.userName.eq(userName))
                .fetchOne());
    }

    // Todo: 密碼加密
    //註冊
    @Override
    @Transactional
    public Option<User> createUser(UserCreate creation) {

        //檢查帳號密碼是否被重複創建
        if (userRepository.existsByUserName(creation.getUserName())) {
            throw new BadRequestException(ApiErrorCode.USER_EXISTED);
        }
        if (userRepository.existsByUserPwd(creation.getUserPwd())) {
            throw new BadRequestException(ApiErrorCode.PASSWORD_EXISTED);
        }

        //密碼加密
        String encryptedPassword = passwordEncoder.encode(creation.getUserPwd());

        //創建帳號
        User user = new User();
        user.setUserName(creation.getUserName());
        user.setUserPwd(encryptedPassword);
        User res = userRepository.save(user);
        UserRole userRole = new UserRole();//同時新增使用者角色
        userRole.setId(res.getId());
        userRole.setUserType(0);//初始設定值為0(一般用戶)
        userRoleRepository.save(userRole);
        return Option.of(res);
    }

    //會員登入
    @Override
    public Option<User> userLogin(UserLogin body) {

        Option<User> userOption = getUserByName(body.getUserName());
        if (userOption.isDefined()) {
            User user = userOption.get();
            if (checkPassword(body, user)) {
                return Option.of(user);
            }
        }

        return Option.none();
    }

    public boolean checkPassword(UserLogin user, User dbUser) {

        String inputPassword = user.getUserPwd();
        return passwordEncoder.matches(inputPassword, dbUser.getUserPwd());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.getUserByUserName(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Fetch UserRole using userId
        UserRole userRole = userRoleRepository.findById(userEntity.getId()).orElse(null);
        if (userRole == null) {
            throw new UsernameNotFoundException("User role not found for user with username: " + username);
        }

        return new CustomUserDetails(userEntity.getUserName(), userEntity.getUserPwd(), String.valueOf(userRole.getUserType()));
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.getUserByName(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found: " + username);
//        }
//
//        // Assuming you have a User class that implements UserDetails
//        return (UserDetails) user;
//    }


//    @Override
//    public Option<User> getById(@Param("user_id") String id) {
//        Option<User> theUser = userRepository.findById(id);
//        if (theUser == null){
//            throw  new RuntimeException("Can't find the userID:"+ id);
//        }
//        Optional<User> optionalUser = userRepository.findById(id);
//        if (optionalUser.isPresent()) {
//            User dbUser = optionalUser.get();
//
//            dbUser.setUserName(theUser.get().getUserName());
//            dbUser.setUserPwd(theUser.get().getUserPwd());
//
//            User saveUser = userRepository.save(dbUser);
//            return Option.of(saveUser);
//        } else {
//            throw new RuntimeException("Can't find UserId: " + id);
//        }
//
//    }


}
