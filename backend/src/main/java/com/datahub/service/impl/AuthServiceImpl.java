package com.datahub.service.impl;

import com.datahub.dto.LoginRequest;
import com.datahub.dto.LoginResponse;
import com.datahub.dto.RegisterRequest;
import com.datahub.entity.Developer;
import com.datahub.repository.DeveloperRepository;
import com.datahub.security.JwtUtil;
import com.datahub.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final DeveloperRepository devRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(DeveloperRepository devRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.devRepo = devRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        if (devRepo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已被注册");
        }
        if (devRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        Developer dev = new Developer();
        dev.setUsername(request.getUsername());
        dev.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        dev.setEmail(request.getEmail());
        dev.setRole(Developer.Role.valueOf(request.getRole().toUpperCase()));
        dev.setApiKey("dh_" + UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        dev.setAvatar("https://api.dicebear.com/7.x/initials/svg?seed=" + request.getUsername());

        dev = devRepo.save(dev);

        String token = jwtUtil.generateToken(dev.getDevId(), dev.getUsername(), dev.getRole().name());

        return new LoginResponse(token, dev.getDevId(), dev.getUsername(),
                dev.getEmail(), dev.getRole().name(), dev.getAvatar());
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Developer dev = devRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        if (!passwordEncoder.matches(request.getPassword(), dev.getPasswordHash())) {
            throw new RuntimeException("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(dev.getDevId(), dev.getUsername(), dev.getRole().name());

        return new LoginResponse(token, dev.getDevId(), dev.getUsername(),
                dev.getEmail(), dev.getRole().name(), dev.getAvatar());
    }
}
