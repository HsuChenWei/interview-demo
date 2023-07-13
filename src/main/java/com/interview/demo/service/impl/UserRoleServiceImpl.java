package com.interview.demo.service.impl;

import com.interview.demo.entity.UserRole;
import com.interview.demo.repository.UserRoleRepository;
import com.interview.demo.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;


    @Override
    public UserRole save(UserRole theUserRole) {
        return userRoleRepository.save(theUserRole);
    }

    @Override
    public Optional<UserRole> findByRoleId(String theRoleId) {
        return userRoleRepository.findByRoleId(theRoleId);
    }
}
