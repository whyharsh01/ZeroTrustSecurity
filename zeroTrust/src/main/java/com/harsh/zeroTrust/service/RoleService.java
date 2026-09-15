package com.harsh.zeroTrust.service;

import com.harsh.zeroTrust.entity.Permission;
import com.harsh.zeroTrust.entity.Role;
import com.harsh.zeroTrust.repository.PermissionRepository;
import com.harsh.zeroTrust.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository , PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public void setRole(Role role) {
        roleRepository.save(role);
    }

    public void addPermissionToRole(Long roleId, String permissionName) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Permission permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        role.getPermissions().add(permission);

        roleRepository.save(role);
    }

}
