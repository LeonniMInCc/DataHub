package com.datahub.service;

import com.datahub.dto.LoginRequest;
import com.datahub.dto.LoginResponse;
import com.datahub.dto.RegisterRequest;

public interface AuthService {
    LoginResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
