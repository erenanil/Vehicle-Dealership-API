package com.anileren.handler;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.anileren.exception.BaseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiError<String>> handleBaseException(BaseException baseException, WebRequest request){
       return ResponseEntity.badRequest().body(createApiError(baseException.getMessage(), request));
    }
    
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError<Map<String,List<String>>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request){

        Map<String, List<String>> map = new HashMap<>();

        for (ObjectError objError : ex.getBindingResult().getAllErrors()) {
            
            FieldError fieldError = (FieldError) objError;
            String fieldName = fieldError.getField();

            if (map.containsKey(fieldName)) {
                map.put(fieldName, addValue(map.get(fieldName), objError.getDefaultMessage()));
            }else{
                map.put(fieldName,  addValue(new ArrayList<>(), objError.getDefaultMessage()));
            }
        }

        return ResponseEntity.badRequest().body(createApiError(map, request));

    }

    public List<String> addValue(List<String> list, String newValue){
        list.add(newValue);
        return list;

    }

    public <E> ApiError<E> createApiError(E messsage , WebRequest request){
        ApiError<E> apiError = new ApiError<>();

        apiError.setStatus(HttpStatus.BAD_REQUEST.value());

        Exception<E> exception = new Exception<>();

        exception.setCreateTime(new Date());
        exception.setHostName(getHostname());
        exception.setPath(request.getDescription(false));
        exception.setMessage(messsage);

        apiError.setException(exception);

        return apiError;
    }

    public String getHostname(){
        try {
            return Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return " ";
    }

}
