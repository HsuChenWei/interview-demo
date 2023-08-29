package com.interview.demo.service.impl;

import com.interview.demo.entity.QUser;
import com.interview.demo.entity.User;
import com.interview.demo.entity.UserRole;
import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.error.ForbiddenRequestException;
import com.interview.demo.model.User.UserCreate;
import com.interview.demo.model.User.UserLogin;
import com.interview.demo.repository.UserRepository;
import com.interview.demo.repository.UserRoleRepository;
import com.interview.demo.repository.querydsl.QuerydslRepository;
import com.interview.demo.service.UserService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
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
        //創建帳號
        User user = new User();
        user.setUserName(creation.getUserName());
        user.setUserPwd(creation.getUserPwd());
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
        try {
            Option<User> userOption = getUserByName(body.getUserName());
            if (userOption.isDefined()) {
                User user = userOption.get();
                if (user.getUserPwd().equals(body.getUserPwd())) {
                    return Option.of(user);
                }
            }
        } catch (DisabledException ex) {
            throw new ForbiddenRequestException(ApiErrorCode.USER_DISABLED, ex);
        } catch (LockedException ex) {
            throw new ForbiddenRequestException(ApiErrorCode.USER_LOCKED, ex);
        } catch (BadCredentialsException ex) {
            throw new ForbiddenRequestException(ApiErrorCode.BAD_CREDENTIAL, ex);
        }
        return Option.none();
    }


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
