package com.anileren.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anileren.controller.IRestCurrencyRatesController;
import com.anileren.controller.RestBaseController;
import com.anileren.controller.RootEntity;
import com.anileren.dto.CurrencyRatesResponse;
import com.anileren.service.ICurrencyRatesService;

@RestController
@RequestMapping("/rest/api")
public class RestCurrencyRatesControllerImpl extends RestBaseController implements IRestCurrencyRatesController{
    @Autowired
    ICurrencyRatesService currencyRatesService;

    @GetMapping("/currency-rates")
    @Override
    public RootEntity<CurrencyRatesResponse> getCurrencyRates
    (@RequestParam("startDate") String startDate,
    @RequestParam("endDate") String endDate) {
        return ok(currencyRatesService.getCurrencyRates(startDate, endDate));
    }
    
}
