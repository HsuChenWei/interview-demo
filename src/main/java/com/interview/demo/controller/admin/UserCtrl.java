package com.interview.demo.controller.admin;

import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.Security.TokenPair;
import com.interview.demo.model.User.UserCreate;
import com.interview.demo.model.User.UserDto;
import com.interview.demo.model.User.UserList;
import com.interview.demo.model.User.UserLogin;
import com.interview.demo.model.wrapper.RespWrapper;
import com.interview.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Option;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/user")
@Tag(name = "Admin - User", description = "會員功能")
public class UserCtrl {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;



    //會員登入(完成)
    @Operation(summary = "會員登入")
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/login")
    public RespWrapper<TokenPair> userLogin(@RequestBody UserLogin body) {
        Option<TokenPair> userOption = userService.userLogin(body);

        return userOption
                .map(user -> RespWrapper.success(modelMapper.map(user, TokenPair.class)))
                .getOrElseThrow(() -> new BadRequestException(ApiErrorCode.USERNAME_OR_PASSWORD_ERROR));
    }

    //會員註冊(完成)
    @Operation(summary = "會員註冊")
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public RespWrapper<UserDto> register(@Validated @RequestBody UserCreate body) {
        return userService.createUser(body)
                .map(u -> modelMapper.map(u, UserDto.class))
                .map(RespWrapper::success)
                .get();
    }


    //查詢所有會員資料(完成)
    @Operation(summary = "查詢所有會員資料")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public RespWrapper<List<UserList>> findAllUser(){
        return RespWrapper.success(userService.getAllUserWithRole()
                .stream()
                .map(u -> modelMapper.map(u, UserList.class))
                .collect(Collectors.toList()));
    }

    //查詢單一會員資料(完成)
    @Operation(summary = "查詢單一會員資料")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public RespWrapper<UserList> findUserById(@PathVariable @Parameter(description = "設定會員 ID", required = true) String id){
        return userService.getUserByIdWithRole(id)
                .map(user -> modelMapper.map(user, UserList.class))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new BadRequestException(ApiErrorCode.USER_NOT_FOUND));
    }





    // Todo: 確認角色權限修改是否整合在這.
    /**
     * NOTE:
     *      1. Url 改: [Method] /api/user/{userId}/role/{roleId} or /api/user/{userId}/role?roleId={roleId}
     *      2. 注意角色修改邏輯, 誰能修改誰的角色, 以及角色權限的設計.
     *
     * (PS: keyword: RESTful API, @RequestBody, @RequestParam, @PathVariable)
     */

    //依會員帳號更改會員內容(完成)
//    @Operation(summary = "更新會員資料")
//    @PutMapping("/{userId}")
//    public User updateUserDetail(@PathVariable String userId, @RequestBody User theUser){
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
//        return theUser;
//    }


}