package com.interview.demo.service.impl;

import com.interview.demo.repository.UserRepository;
import com.interview.demo.entity.User;
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

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User theUser) {
        return userRepository.save(theUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(@Param("user_id") String userId) {

        return userRepository.findById(userId);
    }






}
