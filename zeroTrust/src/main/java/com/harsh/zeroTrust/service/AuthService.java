package com.harsh.zeroTrust.service;

import com.harsh.zeroTrust.dto.UserRegisterRequestDto;
import com.harsh.zeroTrust.dto.UserRegisterResponseDto;
import com.harsh.zeroTrust.entity.Role;
import com.harsh.zeroTrust.entity.User;
import com.harsh.zeroTrust.repository.RoleRepository;
import com.harsh.zeroTrust.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserRegisterResponseDto register(UserRegisterRequestDto userRegisterRequestDto) {
        User user = new User();
        user.setUsername(userRegisterRequestDto.getUsername());
        user.setPassword(passwordEncoder.encode(userRegisterRequestDto.getPassword()));
        user.setEnabled(true);


        Role role = roleRepository.findByName("Role_Admin").get();
        user.getRoles().add(role);

        userRepository.save(user);

        UserRegisterResponseDto userRegisterResponseDto = new UserRegisterResponseDto();
        userRegisterResponseDto.setUsername(user.getUsername());
        userRegisterResponseDto.setMessage(user.getUsername() + " saved successfully");

        return userRegisterResponseDto;
    }

}
