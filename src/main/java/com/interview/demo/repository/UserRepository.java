package com.interview.demo.repository;

import com.interview.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUserName(String userName);

    boolean existsByUserPwd(String userPwd);

    Optional<User> findByUserName(String userName);

//    Object findByUserName(Object username);
}
