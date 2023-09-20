package com.interview.demo.controller.admin;


import com.interview.demo.entity.UserRole;
import com.interview.demo.error.ApiErrorCode;
import com.interview.demo.error.BadRequestException;
import com.interview.demo.model.UserRole.UserRoleDto;
import com.interview.demo.model.wrapper.RespWrapper;
import com.interview.demo.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    @Operation(summary = "更改會員角色屬性")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{roleId}")
    public RespWrapper<UserRoleDto> updateUserRole(@PathVariable String roleId, @RequestBody UserRoleDto theRoleId) {
        Optional<UserRole> optionalUserRole = userRoleService.findByRoleId(roleId);

        if (optionalUserRole.isPresent()) {
            UserRole dbUserRole = optionalUserRole.get();

            if (theRoleId != null) {
                dbUserRole.setUserType(Integer.parseInt(theRoleId.getUserType()));

                UserRole updatedUserRole = userRoleService.save(dbUserRole);

                //轉換前台顯示的欄位，UserRole參照User，用UserRoleDto去除前台userPwd欄位
                UserRoleDto updatedUserRoleDto = modelMapper.map(updatedUserRole, UserRoleDto.class);

                return RespWrapper.success(updatedUserRoleDto);
            } else {
                throw new BadRequestException(ApiErrorCode.NULL);
            }
        } else {
            throw new BadRequestException(ApiErrorCode.ROLE_NOT_FOUND);
        }
    }

}
