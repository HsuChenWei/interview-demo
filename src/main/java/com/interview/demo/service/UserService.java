package com.interview.demo.service;

import com.interview.demo.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User theUser);

    List<User> findAll();

    Optional<User> findById(String userId);

}
