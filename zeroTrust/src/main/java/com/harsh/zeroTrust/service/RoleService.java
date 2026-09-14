package com.harsh.zeroTrust.service;

import com.harsh.zeroTrust.entity.Role;
import com.harsh.zeroTrust.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void setRole(Role role) {
        roleRepository.save(role);
    }

//    public RoleRegisterResponeDto roleRegisterResponeDto(RoleReqisterRequestDto roleRequestRespondDto){
//        Role role = new Role();
//        role.setId((long) roleRequestRespondDto.getId());
//        role.setName(roleRequestRespondDto.getRoleName());
//
//        roleRepository.save(role);
//        RoleRegisterResponeDto roleRegisterResponeDto = new RoleRegisterResponeDto();
//        roleRegisterResponeDto.setRole(role.getName());
//        roleRegisterResponeDto.setMessage("Role saved Successfuilly");
//
//        return roleRegisterResponeDto;
//    }

}
