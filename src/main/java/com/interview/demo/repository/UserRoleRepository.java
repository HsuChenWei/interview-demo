package com.interview.demo.repository;

import com.interview.demo.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {
//    UserRole findByUserId(String userId);

    @Query("SELECT b FROM UserRole b WHERE b.id = :role_id")
    Optional<UserRole> findByRoleId(@Param("role_id") String id);
}
