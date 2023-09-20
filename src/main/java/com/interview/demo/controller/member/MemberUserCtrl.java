package com.interview.demo.controller.member;

import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.Security.TokenPair;
import com.interview.demo.model.User.UserCreate;
import com.interview.demo.model.User.UserDto;
import com.interview.demo.model.User.UserLogin;
import com.interview.demo.model.wrapper.RespWrapper;
import com.interview.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Option;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/member/user")
@Tag(name = "Member - User", description = "會員功能")
public class MemberUserCtrl {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;



    //會員登入(完成)
    @Operation(summary = "會員登入")
    @PostMapping("/login")
    public RespWrapper<TokenPair> userLogin(@RequestBody UserLogin body) {
        Option<TokenPair> userOption = userService.userLogin(body);

        return userOption
                .map(user -> RespWrapper.success(modelMapper.map(user, TokenPair.class)))
                .getOrElseThrow(() -> new BadRequestException(ApiErrorCode.USERNAME_OR_PASSWORD_ERROR));
    }

    @Operation(summary = "會員註冊")
    @PostMapping("/register")
    public RespWrapper<UserDto> register(@Validated @RequestBody UserCreate body) {
        return userService.createUser(body)
                .map(u -> modelMapper.map(u, UserDto.class))
                .map(RespWrapper::success)
                .get();
    }

    @Operation(summary = "會員登出")
    @PostMapping("/logout")
    public ResponseEntity<String> Logout(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            authentication.setAuthenticated(false);
        }
//        request.getSession().invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }


    // Todo: 確認角色權限修改是否整合在這.
    /**
     * NOTE:
     *      1. Url 改: [Method] /api/user/{userId}/role/{roleId} or /api/user/{userId}/role?roleId={roleId}
     *      2. 注意角色修改邏輯, 誰能修改誰的角色, 以及角色權限的設計.
     *
     * (PS: keyword: RESTful API, @RequestBody, @RequestParam, @PathVariable)
     */

}
