package com.anileren.controller;

import com.anileren.dto.CurrencyRatesResponse;

public interface IRestCurrencyRatesController {
    RootEntity<CurrencyRatesResponse> getCurrencyRates(String startDate, String endDate);
}
