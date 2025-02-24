package com.anileren.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrencyRatesItems {
     
    //Json property ile dışarıdan gelen verinin ismini değişken ismine setlenmesi sağlanır.
    @JsonProperty("Tarih")
    private String date;

    @JsonProperty("TP_DK_USD_A")
    private String usd;

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
