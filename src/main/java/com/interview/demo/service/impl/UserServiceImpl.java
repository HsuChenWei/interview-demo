package com.interview.demo.service.impl;

import com.interview.demo.entity.QUser;
import com.interview.demo.entity.User;
import com.interview.demo.entity.UserRole;
import com.interview.demo.model.User.UserCreate;
import com.interview.demo.repository.UserRepository;
import com.interview.demo.repository.UserRoleRepository;
import com.interview.demo.repository.querydsl.QuerydslRepository;
import com.interview.demo.service.UserService;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Todo: 缺少 @Transactional (可以去查看看什麼是資料庫的交易機制, 在程式端通常 CUD 必須在交易中.)
    // Todo: 密碼加密
    @Override
    @Transactional
    public Option<User> createUser(UserCreate creation) {
        User user = new User();
        user.setUserPwd(creation.getUserPwd());
        user.setUserName(creation.getUserName());
        User res = userRepository.save(user);
        UserRole userRole = new UserRole();//同時新增使用者角色
        userRole.setId(res.getId());
        userRole.setUserType(0);//初始設定值為0(一般用戶)
        userRoleRepository.save(userRole);
        return Option.of(res);
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
