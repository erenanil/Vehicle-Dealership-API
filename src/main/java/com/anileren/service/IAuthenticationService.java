package com.anileren.service;

import com.anileren.dto.AuthRequest;
import com.anileren.dto.AuthResponse;
import com.anileren.dto.DtoUser;
import com.anileren.dto.RefreshTokenRequest;

public interface IAuthenticationService {
    public DtoUser register(AuthRequest input);

    public AuthResponse authenticate(AuthRequest input);

    public AuthResponse refreshToken(RefreshTokenRequest input);        
}
