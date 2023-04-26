package org.LamberM.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public static class RestValidationException extends RuntimeException {
        public RestValidationException(String message) {
            super(message);
        }
    }

    //http://localhost:8080/api/exchange/rates/average/{CurrencyCode}/{date}
    @GetMapping("/api/exchange/rates/average/{currencyCode}/{date}")
    public ResponseEntity<ExchangeRateResponse> provideAverageRate(@PathVariable("currencyCode") String currencyCode, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws Exception {
        Currency currency = Currency.getInstance(currencyCode.toUpperCase());
        if (date.isAfter(LocalDate.now())) {
            throw new RestValidationException("Internal Error");
            //Date can't be later than today
        }
        if (!currencyCodeIsThreeLetter(currencyCode)) {
            throw new RestValidationException("Internal Error");
            //Currency code can't be more or less than 3 three letters
        }
        if (!plnCanBeUse(currencyCode)) {
            throw new RestValidationException("Internal Error");
            //PLN is a comparable currency
        }
        BigDecimal averageRate = averageExchangeRateService.getResponse(currency, date);

        return ResponseEntity.ok(new ExchangeRateResponse(averageRate));
    }

    //http://localhost:8080/api/exchange/rates/majorDifferenceBuyAsk/{currencyCode}/{topCount}
    @GetMapping("api/exchange/rates/majorDifferenceBuyAsk/{currencyCode}/{topCount}")
    public ResponseEntity<ExchangeRateResponse> provideMajorDifferenceBuyAskRate(@PathVariable("currencyCode") String currencyCode, @PathVariable("topCount") int topCount) throws Exception {
        if (topCountIsMoreThanAssumption(topCount) || topCountIsLessOrEqualZero(topCount)) {
            throw new RestValidationException("Internal Error");
            //Top count can't be more than 255 and less or equal 0
        }
        if (!currencyCodeIsThreeLetter(currencyCode)) {
            throw new RestValidationException("Internal Error");
            //Currency code can't be more or less than 3 three letters
        }
        if (!plnCanBeUse(currencyCode)) {
            throw new RestValidationException("Internal Error");
            //PLN is a comparable currency
        }
        Currency currency = Currency.getInstance(currencyCode.toUpperCase());
        BigDecimal majorDifferenceBuyAskRate = majorDifferenceBuyAskRateService.getResponse(currency, topCount);
        return ResponseEntity.ok(new ExchangeRateResponse(majorDifferenceBuyAskRate));
    }

    //http://localhost:8080/api/exchange/rates/minMaxAverageValue/{currencyCode}/{topCount}
    @GetMapping("api/exchange/rates/minMaxAverageValue/{currencyCode}/{topCount}")
    public ResponseEntity<BigDecimalResponses> provideMinMaxAverageValue(@PathVariable("currencyCode") String currencyCode, @PathVariable("topCount") int topCount) throws Exception {
        if (topCountIsMoreThanAssumption(topCount) || topCountIsLessOrEqualZero(topCount)) {
            throw new RestValidationException("Internal Error");
            //Top count can't be more than 255 and less or equal 0
        }
        if (!currencyCodeIsThreeLetter(currencyCode)) {
            throw new RestValidationException("Internal Error");
            //Currency code can't be more or less than 3 three letters
        }
        if (!plnCanBeUse(currencyCode)) {
            throw new RestValidationException("Internal Error");
            //PLN is a comparable currency
        }
        Currency currency = Currency.getInstance(currencyCode.toUpperCase());
        BigDecimalResponses minMaxAverageValue = minMaxAverageValueService.getResponse(currency, topCount);
        return ResponseEntity.ok(new BigDecimalResponses(minMaxAverageValue.min(), minMaxAverageValue.max()));
    }

    private boolean topCountIsMoreThanAssumption(int topCount) {
        return topCount > 255;
    }

    private boolean topCountIsLessOrEqualZero(int topCount) {
        return topCount <= 0;
    }

    private boolean currencyCodeIsThreeLetter(String currencyCode) {
        return currencyCode.length() == 3;
    }

    private boolean plnCanBeUse(String currencyCode) {
        return !currencyCode.equals("pln");
    }
}
