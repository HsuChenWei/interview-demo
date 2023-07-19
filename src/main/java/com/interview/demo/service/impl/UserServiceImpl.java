package com.interview.demo.service.impl;

import com.interview.demo.entity.UserRole;
import com.interview.demo.repository.UserRepository;
import com.interview.demo.entity.User;
import com.interview.demo.repository.UserRoleRepository;
import com.interview.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public User save(User theUser) {
        User dbUser = userRepository.save(theUser);//註冊會員資料

        // 創建UserRole
        UserRole userRole = new UserRole();
        userRole.setId(dbUser.getId()); // 讓user_id存至role_id
        userRole.setUserType(0); // 設置註冊新用戶類型為0

        // 保存用戶角色
        userRoleRepository.save(userRole);
        return dbUser;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(@Param("user_id") String userId) {
        Optional<User> theUser = userRepository.findById(userId);
        if (theUser == null){
            throw  new RuntimeException("Can't find the userID:"+ userId);
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User dbUser = optionalUser.get();

            dbUser.setUserName(theUser.get().getUserName());
            dbUser.setUserPwd(theUser.get().getUserPwd());

            User saveUser = userRepository.save(dbUser);
            return Optional.of(saveUser);
        } else {
            throw new RuntimeException("Can't find UserId: " + userId);
        }

    }






}
