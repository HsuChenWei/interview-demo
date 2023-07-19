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

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public UserRole save(UserRole theUserRole) {
        return userRoleRepository.save(theUserRole);
    }

    @Override
    public Optional<UserRole> findByRoleId(String theRoleId) {
//        Optional<UserRole> optionalUserRole = userRoleService.findByRoleId(theRoleId);

        //getUserType會報錯應該是db是int，這裡只能用string(db可能要改Varchar)
//        if (optionalUserRole.isPresent()) {
//            UserRole dbUserRole = optionalUserRole.get();
//
//            dbUserRole.setUserType(theRoleId.getUserType);
//
//            UserRole saveUserRole = userRoleService.save(dbUserRole);
//            return Optional.of(saveUserRole);
//        } else {
//            throw new RuntimeException("Can't find UserId: " + theRoleId);
//        }

        return userRoleRepository.findByRoleId(theRoleId);
    }
}
