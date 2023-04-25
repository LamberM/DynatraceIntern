package org.LamberM.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.LamberM.services.AverageExchangeRateService;
import org.LamberM.services.MajorDifferenceBuyAskRateService;
import org.LamberM.services.MinMaxAverageValueService;
import org.LamberM.utils.BigDecimalResponses;
import org.LamberM.utils.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@RestController
public class ProvideController {
    private final AverageExchangeRateService averageExchangeRateService;
    private final MajorDifferenceBuyAskRateService majorDifferenceBuyAskRateService;
    private final MinMaxAverageValueService minMaxAverageValueService;

    @Autowired
    public ProvideController(AverageExchangeRateService averageExchangeRateService, MajorDifferenceBuyAskRateService majorDifferenceBuyAskRateService, MinMaxAverageValueService minMaxAverageValueService) {
        this.averageExchangeRateService = averageExchangeRateService;
        this.majorDifferenceBuyAskRateService = majorDifferenceBuyAskRateService;
        this.minMaxAverageValueService = minMaxAverageValueService;
    }

    @GetMapping("/api/exchange/rates/average/{currencyCode}/{date}")
    public ResponseEntity<ExchangeRateResponse> provideAverageRate(@RequestParam("currencyCode")Currency currencyCode, @RequestParam LocalDate date) throws JsonProcessingException {
        if (date.isAfter(LocalDate.now())){
            return ResponseEntity.badRequest()
                    .build();
        }
        BigDecimal averageRate = averageExchangeRateService.getResponse(currencyCode,date);

        return ResponseEntity.ok(new ExchangeRateResponse(averageRate));
    }
    @GetMapping("api/exchange/rates/majorDifferenceBuyAsk/{currencyCode}/{topCount}")
    public ResponseEntity<ExchangeRateResponse> provideMajorDifferenceBuyAskRate(@RequestParam("currencyCode")Currency currencyCode, @RequestParam int topCount){
        BigDecimal majorDifferenceBuyAskRate = majorDifferenceBuyAskRateService.getResponse(currencyCode,topCount);
        return ResponseEntity.ok(new ExchangeRateResponse(majorDifferenceBuyAskRate));
    }
    @GetMapping("api/exchange/rates/minMaxAverageValue/{currencyCode}/{topCount}")
    public ResponseEntity<BigDecimalResponses> provideMinMaxAverageValue(@RequestParam("currencyCode")Currency currencyCode, @RequestParam int topCount){
        BigDecimalResponses minMaxAverageValue = minMaxAverageValueService.getResponse(currencyCode,topCount);
        return ResponseEntity.ok(new BigDecimalResponses(minMaxAverageValue.value1(),minMaxAverageValue.value2()));
    }
}
