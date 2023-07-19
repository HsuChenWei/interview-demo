package com.interview.demo.contorller.admin;


import com.interview.demo.entity.UserRole;
import com.interview.demo.service.UserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user-role")
@Tag(name = "User - Role", description = "會員角色")
public class UserRoleCtrl {

    @Autowired
    private UserRoleService userRoleService;


    //更改會員角色屬性(未完成，ServiceImpl有型別問題)
    //不改到service是可以動
    @Operation(summary ="更改會員角色屬性")
    @PutMapping("/{roleId}")
    public UserRole updateUserRole(@PathVariable String roleId,
                                        @RequestBody UserRole theRoleId){
        Optional<UserRole> optionalUserRole = userRoleService.findByRoleId(roleId);

        if (optionalUserRole.isPresent()) {
            UserRole dbUserRole = optionalUserRole.get();

            dbUserRole.setUserType(theRoleId.getUserType());

            UserRole saveUserRole = userRoleService.save(dbUserRole);
            return saveUserRole;
        } else {
            throw new RuntimeException("Can't found UserId：" + roleId);
        }
//        UserRole updatedUserRole = new UserRole();
//        updatedUserRole.setId(roleId);
//        return updatedUserRole;
    }
}
