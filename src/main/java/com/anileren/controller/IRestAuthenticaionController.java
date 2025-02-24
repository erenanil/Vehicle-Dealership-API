package com.anileren.controller;

import com.anileren.dto.AuthRequest;
import com.anileren.dto.AuthResponse;
import com.anileren.dto.DtoUser;
import com.anileren.dto.RefreshTokenRequest;

public interface IRestAuthenticaionController {
    RootEntity<DtoUser> register(AuthRequest input);
    RootEntity<AuthResponse> authenticate(AuthRequest input);
    RootEntity<AuthResponse> refreshToken(RefreshTokenRequest input);
}
