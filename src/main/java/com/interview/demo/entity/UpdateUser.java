package com.interview.demo.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "user_id")
@Data
public class UpdateUser {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.interview.demo.hibernate.SnowflakeIDGenerator")
    @Column(name = "user_id")
    private String id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_pwd")
    private String userPwd;

}
