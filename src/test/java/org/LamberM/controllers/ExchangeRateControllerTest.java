package org.LamberM.controllers;

import org.LamberM.UnitTest;
import org.LamberM.error.handler.RestErrorResponse;
import org.LamberM.error.handler.RestExceptionHandler;
import org.LamberM.services.AverageExchangeRateService;
import org.LamberM.services.MajorDifferenceBuyAskRateService;
import org.LamberM.services.MinMaxAverageValueService;
import org.LamberM.utils.BigDecimalResponses;
import org.LamberM.utils.ExchangeRateResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.net.http.HttpConnectTimeoutException;
import java.time.LocalDate;
import java.util.Currency;

import static org.mockito.Mockito.*;


public class ExchangeRateControllerTest implements UnitTest {
    @InjectMocks
    ExchangeRateController systemUnderTest;
    @Mock
    AverageExchangeRateService averageExchangeRateServiceMock;
    @Mock
    MajorDifferenceBuyAskRateService majorDifferenceBuyAskRateServiceMock;
    @Mock
    MinMaxAverageValueService minMaxAverageValueService;

    @Test
    void givenCurrencyCodeDate_whenProvideAverageRate_thenCorrectWork() throws Exception {
        //given
        LocalDate date = LocalDate.parse(("2023-04-21"));
        Currency currency = Currency.getInstance("AUD");
        String currencyCode = "aud";
        ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse(BigDecimal.valueOf(1));
        when(averageExchangeRateServiceMock.calculateAverageExchangeRate(currency,date)).thenReturn(exchangeRateResponse);
        //when
        ResponseEntity<ExchangeRateResponse> returnValue = systemUnderTest.provideAverageRate(currencyCode, date);
        //then
        verify(averageExchangeRateServiceMock).calculateAverageExchangeRate(currency, date);
        Assertions.assertEquals(returnValue.getBody(),exchangeRateResponse);
    }

    @Test
    void givenPlnCurrencyCodeDate_whenProvideAverageRate_thenRestErrorResponse() {
        //given
        LocalDate date = LocalDate.parse(("2023-04-21"));
        String currencyCode = "pln";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideAverageRate(currencyCode, date));
    }

    @Test
    void given2lettersCurrencyCodeDate_whenProvideAverageRate_thenRestErrorResponse() {
        //given
        LocalDate date = LocalDate.parse(("2023-04-21"));
        String currencyCode = "ad";
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> systemUnderTest.provideAverageRate(currencyCode, date));
    }

    @Test
    void given4lettersCurrencyCodeDate_whenProvideAverageRate_thenRestErrorResponse() {
        //given
        LocalDate date = LocalDate.parse(("2023-04-21"));
        String currencyCode = "test";
        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> systemUnderTest.provideAverageRate(currencyCode, date));

    }

    @Test
    void givenCurrencyCodeAfterThanLocalDate_whenProvideAverageRate_thenRestErrorResponse() {
        //given
        LocalDate date = LocalDate.now().plusDays(1);
        String currencyCode = "aud";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideAverageRate(currencyCode, date));

    }


    /////////////////////////////////// MajorDifferenceTests//////////////////////////////////////////////////
    @Test
    void givenCurrencyCodeTopCount_whenProvideMajorDifferenceBuyAskRate_thenCorrectWork() throws Exception {
        //given
        int topCount = 10;
        String currencyCode = "aud";
        Currency currency = Currency.getInstance("AUD");
        ExchangeRateResponse exchangeRateResponse = new ExchangeRateResponse(BigDecimal.valueOf(1));
        when(majorDifferenceBuyAskRateServiceMock.calculateMajorDifferenceBuyAskRate(currency,topCount)).thenReturn(exchangeRateResponse);
        //when
        ResponseEntity<ExchangeRateResponse> returnValue = systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount);
        //then
        verify(majorDifferenceBuyAskRateServiceMock).calculateMajorDifferenceBuyAskRate(currency, topCount);
        Assertions.assertEquals(returnValue.getBody(),exchangeRateResponse);
    }

    @Test
    void givenPlnCurrencyCodeTopCount_whenProvideMajorDifferenceBuyAskRate_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "pln";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount));
    }

    @Test
    void given2lettersCurrencyCodeTopCount_whenProvideMajorDifferenceBuyAskRate_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "ad";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount));
    }

    @Test
    void given4lettersCurrencyCodeTopCount_whenProvideMajorDifferenceBuyAskRate_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "test";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount));
    }

    @Test
    void givenCurrencyCodeMoreThanConditionTopCount_whenProvideMajorDifferenceBuyAskRate_thenRestErrorResponse() {
        //given
        int topCount = 256;
        String currencyCode = "aud";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount));

    }

    @Test
    void givenCurrencyCodeLessThanConditionTopCount_whenProvideMajorDifferenceBuyAskRate_thenRestErrorResponse() {
        //given
        int topCount = 0;
        String currencyCode = "aud";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount));
    }

    ////////////////////////////////////////MinMaxAverageTests //////////////////////////////////////////////////////////////
    @Test
    void givenCurrencyCodeTopCount_whenProvideMinMaxAverageValue_thenCorrectWork() throws Exception {
        //given
        int topCount = 10;
        String currencyCode = "aud";
        Currency currency = Currency.getInstance("AUD");
        BigDecimalResponses bigDecimalResponses = new BigDecimalResponses(BigDecimal.valueOf(1),BigDecimal.valueOf(2));
        when(minMaxAverageValueService.calculateMinMaxAverageValue(currency,topCount)).thenReturn(bigDecimalResponses);
        //when
        ResponseEntity<BigDecimalResponses> returnValues = systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount);
        //then
        verify(minMaxAverageValueService).calculateMinMaxAverageValue(currency, topCount);
        Assertions.assertEquals(returnValues.getBody(),bigDecimalResponses);
    }

    @Test
    void givenPlnCurrencyCodeTopCount_whenProvideMinMaxAverageValue_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "pln";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount));

    }

    @Test
    void given2lettersCurrencyCodeTopCount_whenProvideMinMaxAverageValue_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "ad";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount));

    }

    @Test
    void given4lettersCurrencyCodeTopCount_whenProvideMinMaxAverageValue_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "test";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount));
    }

    @Test
    void givenCurrencyCodeMoreThanConditionTopCount_whenProvideMinMaxAverageValue_thenRestErrorResponse() {
        //given
        int topCount = 256;
        String currencyCode = "aud";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount));
    }

    @Test
    void givenCurrencyCodeLessThanConditionTopCount_whenProvideMinMaxAverageValue_thenRestErrorResponse() {
        //given
        int topCount = 0;
        String currencyCode = "aud";
        //when
        //then
        Assertions.assertThrows(ExchangeRateController.RestValidationException.class, () -> systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount));

    }
}