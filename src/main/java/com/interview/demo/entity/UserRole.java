package com.interview.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_role")
public class UserRole {

    @Id
    @Column(name = "role_id")
    private String id;


    @Column(name = "user_type")
    private int userType;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;

}


