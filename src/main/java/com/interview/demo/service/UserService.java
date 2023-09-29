package com.interview.demo.service;

import com.interview.demo.entity.User;
import com.interview.demo.model.Security.TokenPair;
import com.interview.demo.model.User.UserCreate;
import com.interview.demo.model.User.UserList;
import com.interview.demo.model.User.UserLogin;
import com.interview.demo.model.User.UserUpdate;
import io.vavr.control.Option;

import java.util.List;


public interface UserService {

    List<User> findAllUser();

    List<UserList> getAllUserWithRole();

    Option<User> getUserById(String id);

    Option<UserList> getUserByIdWithRole(String id);

    Option<User> getUserByName(String userName);

    Option<User> createUser(UserCreate creation);

    Option<TokenPair> userLogin(UserLogin body);

    List<String> getRoleTypeByUserId(String userId);

    Option<User> updateUserDetailByUserName(String userName, UserUpdate theUser);
}
