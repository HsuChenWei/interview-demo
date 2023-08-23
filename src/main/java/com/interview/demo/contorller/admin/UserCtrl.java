package com.interview.demo.contorller.admin;

import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.User.UserCreate;
import com.interview.demo.model.User.UserDto;
import com.interview.demo.model.wrapper.RespWrapper;
import com.interview.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "會員功能")
public class UserCtrl {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;


    // Todo: Login API? (PS. 先解決密碼加密問題, 然後在寫 Login)
    /** NOTE: 先用簡單的方式實作, 實作完後再改成 JWT 加上 Spring Security.*/


    // Todo: 補上分頁查詢
    //查詢所有會員資料(完成)
    @Operation(summary = "查詢所有會員資料")
    @GetMapping
    public RespWrapper<List<UserDto>> findAllUser(){
        return RespWrapper.success(userService.findAllUser()
                .stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList()));
    }

    //查詢單一會員資料(完成)
    @Operation(summary = "查詢單一會員資料")
    @GetMapping("/{id}")
    public RespWrapper<UserDto> findUserById(@PathVariable @Parameter(description = "設定會員 ID", required = true) String id){
        return userService.getUserById(id)
                .map(user -> modelMapper.map(user, UserDto.class))
                .map(RespWrapper::success)
                .getOrElseThrow(() -> new BadRequestException(ApiErrorCode.USER_NOT_FOUND));
    }

    // Todo: 註冊時要檢查帳號是否重複.
    @Operation(summary = "會員註冊")
    @PostMapping
    public RespWrapper<UserDto> register(@Validated @RequestBody UserCreate body) {
        return userService.createUser(body)
                .map(u -> modelMapper.map(u, UserDto.class))
                .map(RespWrapper::success)
                .get();
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
