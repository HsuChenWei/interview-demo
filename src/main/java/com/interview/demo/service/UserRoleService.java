package com.interview.demo.service;

import com.interview.demo.entity.UserRole;

import java.util.Optional;

public interface UserRoleService {


    UserRole save(UserRole theUserRole);

    Optional<UserRole> findByRoleId(String theRoleId);

}
