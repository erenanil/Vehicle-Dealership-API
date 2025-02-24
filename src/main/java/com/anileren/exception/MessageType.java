package com.anileren.exception;

import lombok.Getter;

@Getter
public enum MessageType {
    
    NO_RECORD_EXIST("1001","Kayıt bulunamadı"),
    TOKEN_IS_EXPIRED("1002", "Token'ın süresi dolmuştur."),
    USERNAME_NOT_FOUND("1003","Kullanıcı bulunamadı"),
    REFRESH_TOKEN_NOT_FOUND("1004","Refresh Token bulunamadı"),
    REFRESH_TOKEN_IS_EXPIRED("1005","Refresh Token'ın süresi dolmuştur"),
    ADDRESS_NOT_FOUND("1006","Adres bulunamadı"),
    ACCOUNT_NOT_FOUND("1007","Hesap Bulunamadı"),
    CURRENCY_RATES_IS_OCCURED("1008","döviz kuru alınamadı"),
    CUSTOMER_AMOUNT_IS_NOT_ENOUGH("1009","bütçe yeterli değil"),
    CAR_IS_ALREADY_SALED("1010","bu araç zaten satılmış"),
    USERNAME_OR_PASSWORD_INVALID("1004","Kullanıcı adı veya şifre yanlış."),
    GENERAL_EXCEPTION("9999", "Genel bir hata oluştu.");

    private String code;

    private String message;

    MessageType(String code, String message){
        this.code = code;
        this.message = message;
    }

}
