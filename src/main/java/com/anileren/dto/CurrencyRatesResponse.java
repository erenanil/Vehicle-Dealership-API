package com.anileren.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyRatesResponse {

    private Integer totalCount;

    private List<CurrencyRatesItems> items;

    /*
     * {
  "totalCount": 1,
  "items": [
    {
      "Tarih": "14-02-2025",
      "TP_DK_USD_A": "36.0448",
      "TP_DK_EUR_A": "37.5557",
      "TP_DK_CHF_A": "39.587",
      "TP_DK_GBP_A": "44.9389",
      "TP_DK_JPY_A": "23.3503",
      "UNIXTIME": {
        "$numberLong": "1739480400"
      }
    }
  ]
}
     */
}
