package org.LamberM.controllers;

import org.LamberM.services.AverageExchangeRateService;
import org.LamberM.services.MajorDifferenceBuyAskRateService;
import org.LamberM.services.MinMaxAverageValueService;
import org.LamberM.utils.BigDecimalResponses;
import org.LamberM.utils.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Currency;

@RestController
public class ExchangeRateController {
    private final AverageExchangeRateService averageExchangeRateService;
    private final MajorDifferenceBuyAskRateService majorDifferenceBuyAskRateService;
    private final MinMaxAverageValueService minMaxAverageValueService;

    @Autowired
    public ExchangeRateController(AverageExchangeRateService averageExchangeRateService, MajorDifferenceBuyAskRateService majorDifferenceBuyAskRateService, MinMaxAverageValueService minMaxAverageValueService) {
        this.averageExchangeRateService = averageExchangeRateService;
        this.majorDifferenceBuyAskRateService = majorDifferenceBuyAskRateService;
        this.minMaxAverageValueService = minMaxAverageValueService;
    }

    public static class RestValidationException extends RuntimeException {
        public RestValidationException(String message) {
            super(message);
        }
    }

    @GetMapping("/api/exchange/rates/average/{currencyCode}/{date}")
    public ResponseEntity<ExchangeRateResponse> provideAverageRate(@PathVariable("currencyCode") String currencyCode, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws Exception {
        Currency currency = Currency.getInstance(currencyCode.toUpperCase());
        if (date.isAfter(LocalDate.now())) {
            throw new RestValidationException("Date can't be later than today");
        }
        if (!currencyCodeIsThreeLetter(currencyCode)) {
            throw new RestValidationException("Currency code can't be more or less than 3 three letters");
        }
        if (!isPln(currencyCode)) {
            throw new RestValidationException("PLN is a comparable currency");
        }
        ExchangeRateResponse averageRate = averageExchangeRateService.calculateAverageExchangeRate(currency, date);

        return ResponseEntity.ok(averageRate);
    }

    @GetMapping("api/exchange/rates/majorDifferenceBuyAsk/{currencyCode}/{topCount}")
    public ResponseEntity<ExchangeRateResponse> provideMajorDifferenceBuyAskRate(@PathVariable("currencyCode") String currencyCode, @PathVariable("topCount") int topCount) throws Exception {
        if (isTopCountValid(topCount)) {
            throw new RestValidationException("Top count can't be more than 255 and less or equal 0");
        }
        if (!currencyCodeIsThreeLetter(currencyCode)) {
            throw new RestValidationException("Currency code can't be more or less than 3 three letters");
        }
        if (!isPln(currencyCode)) {
            throw new RestValidationException("PLN is a comparable currency");
        }
        Currency currency = Currency.getInstance(currencyCode.toUpperCase());
        ExchangeRateResponse majorDifferenceBuyAskRate = majorDifferenceBuyAskRateService.calculateMajorDifferenceBuyAskRate(currency, topCount);
        return ResponseEntity.ok(majorDifferenceBuyAskRate);
    }

    @GetMapping("api/exchange/rates/minMaxAverageValue/{currencyCode}/{topCount}")
    public ResponseEntity<BigDecimalResponses> provideMinMaxAverageValue(@PathVariable("currencyCode") String currencyCode, @PathVariable("topCount") int topCount) throws Exception {
        if (isTopCountValid(topCount)) {
            throw new RestValidationException("Top count can't be more than 255 and less or equal 0");
        }
        if (!currencyCodeIsThreeLetter(currencyCode)) {
            throw new RestValidationException("Currency code can't be more or less than 3 three letters");
        }
        if (!isPln(currencyCode)) {
            throw new RestValidationException("PLN is a comparable currency");
        }
        Currency currency = Currency.getInstance(currencyCode.toUpperCase());
        BigDecimalResponses minMaxAverageValue = minMaxAverageValueService.calculateMinMaxAverageValue(currency, topCount);
        return ResponseEntity.ok(new BigDecimalResponses(minMaxAverageValue.min(), minMaxAverageValue.max()));
    }

    private boolean isTopCountValid(int topCount) {
        return topCount > 255 || topCount <= 0;
    }


    private boolean currencyCodeIsThreeLetter(String currencyCode) {
        return currencyCode.length() == 3;
    }

    private boolean isPln(String currencyCode) {
        return !currencyCode.equals("pln");
    }

}
