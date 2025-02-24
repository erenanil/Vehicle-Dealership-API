package com.anileren.service;

import com.anileren.dto.CurrencyRatesResponse;

public interface ICurrencyRatesService {
    public CurrencyRatesResponse getCurrencyRates(String startDate, String endDate);
}
