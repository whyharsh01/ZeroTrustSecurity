package com.harsh.zeroTrust.controller;

import com.harsh.zeroTrust.entity.Role;
import com.harsh.zeroTrust.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<String> addRole(@RequestBody Role role) {
        roleService.setRole(role);

        return ResponseEntity.ok("Done");
    }

    @PostMapping("/{roleId}/permissions/{permissionName}")
    public ResponseEntity<String> addPermission(
            @PathVariable Long roleId,
            @PathVariable String permissionName) {

        roleService.addPermissionToRole(roleId, permissionName);

        return ResponseEntity.ok("Permission assigned");
    }
}
