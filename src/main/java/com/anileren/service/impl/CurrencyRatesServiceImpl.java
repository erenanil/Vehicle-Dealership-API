package com.anileren.service.impl;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.anileren.dto.CurrencyRatesResponse;
import com.anileren.exception.BaseException;
import com.anileren.exception.ErrorMessage;
import com.anileren.exception.MessageType;
import com.anileren.service.ICurrencyRatesService;

@Service
public class CurrencyRatesServiceImpl implements ICurrencyRatesService{
    @Override
    public CurrencyRatesResponse getCurrencyRates(String startDate, String endDate) {
        
        String apiKey = System.getenv("TURKIYE_CUMHURIYETI_MERKEZ_BANKASI_API_KEY");

        //https://evds2.tcmb.gov.tr/service/evds/series=TP.DK.USD.A&startDate=14-02-2025&endDate=14-02-2025&type=json
        String root = "https://evds2.tcmb.gov.tr/service/evds/"; //ENV KUR.
        String series ="TP.DK.USD.A";
        String type = "json";
        String endpoint = root+"series="+series+"&startDate="+startDate+"&endDate="+endDate+"&type="+type;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("key", apiKey);

        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

        //isteğimizi atmak için RestTemplate kullanıyoruz. 
        //1. parametre hangi url'e istek atacağımızı belirtir.
        //2. parametre hangi tipte isteğin atlacağını belirtir. Post, get vs.
        //3. parametre httpEntity alıyor bunun içerisinde headerımız da var dolayısıyla secret keyi almış olduk.
        //4. parametre bize geri dönülecek olan tipi ister. 
        try {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CurrencyRatesResponse> response = restTemplate.exchange(endpoint,HttpMethod.GET,httpEntity,
        new ParameterizedTypeReference<CurrencyRatesResponse>(){});
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
    } catch (Exception e) {
        throw new BaseException(new ErrorMessage(MessageType.CURRENCY_RATES_IS_OCCURED, e.getMessage()));
    }
        return null;
    }
    
}
