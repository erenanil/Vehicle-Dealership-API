package com.anileren.service.impl;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.anileren.dto.AuthRequest;
import com.anileren.dto.AuthResponse;
import com.anileren.dto.DtoUser;
import com.anileren.dto.RefreshTokenRequest;
import com.anileren.exception.BaseException;
import com.anileren.exception.ErrorMessage;
import com.anileren.exception.MessageType;
import com.anileren.jwt.JwtService;
import com.anileren.model.RefreshToken;
import com.anileren.model.User;
import com.anileren.repository.RefreshTokenRepository;
import com.anileren.repository.UserRepository;
import com.anileren.service.IAuthenticationService;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationProvider authenticationProvider;

    @Autowired
    JwtService jwtService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    //Register
    private User createUser(AuthRequest input){
        
        User user = new User();
        user.setCreateTime(new Date());
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return user;
    }

    

    @Override
    @Transactional
    public DtoUser register(AuthRequest input) {
        DtoUser dtoUser = new DtoUser();
        
        User savedUser = userRepository.save(createUser(input));
        BeanUtils.copyProperties(savedUser, dtoUser);
      
        return dtoUser;
    }

    // Authenticate
    private RefreshToken createRefreshToken(User user){

        RefreshToken refreshToken = new RefreshToken();
        
        refreshToken.setCreateTime(new Date());
        refreshToken.setUser(user);
        refreshToken.setExpiredDate(new Date(System.currentTimeMillis()+1000*60*60*4));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        return refreshToken;
    }

    @Override
    @Transactional
    public AuthResponse authenticate(AuthRequest input) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword());
            authenticationProvider.authenticate(authenticationToken);

            Optional<User> optional = userRepository.findByUsername(input.getUsername());

            String accessToken = jwtService.generateToken(optional.get());
            RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(optional.get()));
            
            return new AuthResponse(accessToken, savedRefreshToken.getRefreshToken());
        } catch (Exception e) {

            throw new BaseException(new ErrorMessage(MessageType.USERNAME_OR_PASSWORD_INVALID,e.getMessage()));
        
        }
    }

    private boolean isTokenExpired(Date expiredDate){
        return new Date().after(expiredDate);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest input) {

        Optional<RefreshToken> optinalRefreshToken = refreshTokenRepository.findByRefreshToken(input.getRefreshToken());

        if (optinalRefreshToken.isEmpty()) {
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_NOT_FOUND, input.getRefreshToken()));
        }

       
         if (isTokenExpired(optinalRefreshToken.get().getExpiredDate())) {
            throw new BaseException(new ErrorMessage(MessageType.REFRESH_TOKEN_IS_EXPIRED, input.getRefreshToken()));
        }

        User user = optinalRefreshToken.get().getUser();
        String accessToken = jwtService.generateToken(user);

        RefreshToken savedRefreshToken = refreshTokenRepository.save(createRefreshToken(user));

        return new AuthResponse(accessToken,savedRefreshToken.getRefreshToken());


    }






}
