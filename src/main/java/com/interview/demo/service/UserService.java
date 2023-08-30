package com.interview.demo.service;

import com.interview.demo.entity.User;
import com.interview.demo.model.User.UserCreate;
import com.interview.demo.model.User.UserLogin;
import io.vavr.control.Option;

import java.util.List;


public interface UserService {

    List<User> findAllUser();

    Option<User> getUserById(String id);

    Option<User> getUserByName(String userName);


    Option<User> createUser(UserCreate creation);

    Option<User> userLogin(UserLogin body);

}
