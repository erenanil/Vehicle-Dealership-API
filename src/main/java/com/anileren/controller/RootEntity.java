package com.anileren.controller;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(value = Include.NON_NULL)
public class RootEntity <T>{

    private Integer status;
    
    private T payload;
    
    private String errorMessage;


    public static <T> RootEntity<T> ok(T payload){
        RootEntity<T> rootEntity = new RootEntity<>();
        rootEntity.setErrorMessage(null);
        rootEntity.setPayload(payload);
        rootEntity.setStatus(HttpStatus.OK.value());
        
        
        return rootEntity;
    }

    public static <T> RootEntity<T> error(String errorMessage){
        RootEntity<T> rootEntity = new RootEntity<>();
        rootEntity.setStatus(HttpStatus.BAD_REQUEST.value());
        rootEntity.setPayload(null);
        rootEntity.setErrorMessage(errorMessage);

        return rootEntity;
    }
}
