package com.interview.demo.contorller.admin;


import com.interview.demo.entity.UserRole;
import com.interview.demo.model.UserRole.UserRoleDto;
import com.interview.demo.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/role")
@Tag(name = "User - Role", description = "會員角色")
public class UserRoleCtrl {

    @Autowired
    private UserRoleService userRoleService;


    //更改會員角色屬性(未完成，ServiceImpl有型別問題)
    //不改到service是可以動
//    @Operation(summary ="更改會員角色屬性")
//    @PutMapping("/{roleId}")
//    public UserRole updateUserRole(@PathVariable String roleId,
//                                        @RequestBody UserRole theRoleId){
//        Optional<UserRole> optionalUserRole = userRoleService.findByRoleId(roleId);
//
//        if (optionalUserRole.isPresent()) {
//            UserRole dbUserRole = optionalUserRole.get();
//
//            dbUserRole.setUserType(theRoleId.getUserType());
//
//            return userRoleService.save(dbUserRole);
//        } else {
//            throw new RuntimeException("Can't found UserId：" + roleId);
//        }
//    }

    @Operation(summary = "更改會員角色屬性")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{roleId}")
    public ResponseEntity<UserRole> updateUserRole(@PathVariable String roleId, @RequestBody UserRoleDto theRoleId) {
        // 根据 roleId 查找 UserRole 记录
        Optional<UserRole> optionalUserRole = userRoleService.findByRoleId(roleId);

        if (optionalUserRole.isPresent()) {
            UserRole dbUserRole = optionalUserRole.get();

            // 检查传递的 theRoleId 是否为空
            if (theRoleId != null) {
                // 更新用户角色属性
                dbUserRole.setUserType(theRoleId.getUserType());

                // 保存更新后的 UserRole 记录
                UserRole updatedUserRole = userRoleService.save(dbUserRole);

                // 返回成功响应
                return ResponseEntity.ok(updatedUserRole);
            } else {
                // 如果 theRoleId 为空，返回错误响应
                return ResponseEntity.badRequest().body(null);
            }
        } else {
            // 如果找不到匹配的 UserRole 记录，返回 404 Not Found 响应
            return ResponseEntity.notFound().build();
        }
    }

}
