package com.interview.demo.contorller.admin;

import com.interview.demo.entity.Booking;
import com.interview.demo.entity.User;
import com.interview.demo.entity.UserRole;
import com.interview.demo.service.UserRoleService;
import com.interview.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "會員功能")
public class UserCtrl {



    @Autowired
    public UserService userService;
    @Autowired
    public UserRoleService userRoleService;

    //查詢所有會員資料(完成)
    @Operation(summary = "查詢所有會員資料")
    @GetMapping
    public List<User> findAllUser(){
        return userService.findAll();
    }

    //查詢單一會員資料(完成)
    @Operation(summary = "查詢單一會員資料")
    @GetMapping("/{userId}")
    public Optional<User> findUserById(@PathVariable @Parameter(description = "會員 ID", required = true) String userId){
        Optional<User> theUser = userService.findById(userId);
        return theUser;
    }

    @Operation(summary = "會員註冊")
    @PostMapping
    public User register(@RequestBody User theUser) {
        User dbUser = userService.save(theUser);//註冊會員資料
        return dbUser;
    }

    //依會員帳號更改會員內容(完成)
    @Operation(summary = "更新會員資料")
    @PutMapping("/{userId}")
    public User updateUserDetail(@PathVariable String userId, @RequestBody User theUser){
//        Optional<User> getUser = userService.findById(userId, theUser);
//        Optional<User> optionalUser = userService.findById(userId);
//        if (optionalUser.isPresent()) {
//            User dbUser = optionalUser.get();
//
//            dbUser.setUserName(theUser.getUserName());
//            dbUser.setUserPwd(theUser.getUserPwd());
//
//            User saveUser = userService.save(dbUser);
//            return saveUser;
//        } else {
//            throw new RuntimeException("Can't found UserId：" + userId);
//        }
        return theUser;
    }


}