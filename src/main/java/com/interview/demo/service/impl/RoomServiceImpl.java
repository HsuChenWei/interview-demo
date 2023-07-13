package com.interview.demo.service.impl;

import com.interview.demo.entity.Room;
import com.interview.demo.repository.RoomRepository;
import com.interview.demo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;
    @Override
    public Room save(Room room) {
        return null;
    }
}
