package com.interview.demo.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "room")
public class Room {

    @Id
    @Column(name = "room_id")
    private int id;

    @Column(name = "room_name")
    private String roomName;

}
