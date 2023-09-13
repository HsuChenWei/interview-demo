package com.interview.demo.controller.admin;


import com.interview.demo.entity.UserRole;
import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.UserRole.UserRoleDto;
import com.interview.demo.model.wrapper.RespWrapper;
import com.interview.demo.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/role")
@Tag(name = "Admin - User - Role", description = "會員角色")
public class UserRoleCtrl {

    @Autowired
    private UserRoleService userRoleService;

    @Operation(summary = "更改會員角色屬性")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{roleId}")
    public RespWrapper<UserRole> updateUserRole(@PathVariable String roleId, @RequestBody UserRoleDto theRoleId) {
        Optional<UserRole> optionalUserRole = userRoleService.findByRoleId(roleId);

        if (optionalUserRole.isPresent()) {
            UserRole dbUserRole = optionalUserRole.get();

            if (theRoleId != null) {
                dbUserRole.setUserType(Integer.parseInt(theRoleId.getUserType()));

                UserRole updatedUserRole = userRoleService.save(dbUserRole);

                return RespWrapper.success(updatedUserRole);
//                return ResponseEntity.ok(updatedUserRole);
            } else {
                throw new BadRequestException(ApiErrorCode.NULL);
//                return ResponseEntity.badRequest().body(null);
            }
        } else {
            throw new BadRequestException(ApiErrorCode.ROLE_NOT_FOUND);
        }
    }

}
