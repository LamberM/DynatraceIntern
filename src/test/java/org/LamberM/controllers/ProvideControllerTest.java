package org.LamberM.controllers;

import org.LamberM.UnitTest;
import org.LamberM.services.AverageExchangeRateService;
import org.LamberM.services.MajorDifferenceBuyAskRateService;
import org.LamberM.services.MinMaxAverageValueService;
import org.LamberM.utils.BigDecimalResponses;
import org.LamberM.utils.ExchangeRateResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;


public class ProvideControllerTest implements UnitTest {
    @InjectMocks
    ProvideController systemUnderTest;
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
        String currencyCode = "aud";
        //when
        ResponseEntity<ExchangeRateResponse> actual = systemUnderTest.provideAverageRate(currencyCode, date);
        //then
        Assertions.assertNotNull(actual);
    }

    @Test
    void givenPlnCurrencyCodeDate_whenProvideAverageRate_thenRestErrorResponse() {
        //given
        LocalDate date = LocalDate.parse(("2023-04-21"));
        String currencyCode = "pln";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideAverageRate(currencyCode, date));
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
        LocalDate date = LocalDate.parse(("2023-05-28"));
        String currencyCode = "aud";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideAverageRate(currencyCode, date));

    }

    @Test
    void givenCurrencyCodeUncorrectDate_whenProvideAverageRate_thenRestErrorResponse() {
        //given
        LocalDate date = LocalDate.parse(("2023-04-22"));
        String currencyCode = "aud";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideAverageRate(currencyCode, date));

    }

    /////////////////////////////////// MajorDifferenceTests//////////////////////////////////////////////////
    @Test
    void givenCurrencyCodeTopCount_whenProvideMajorDifferenceBuyAskRate_thenCorrectWork() throws Exception {
        //given
        int topCount = 10;
        String currencyCode = "aud";
        //when
        ResponseEntity<ExchangeRateResponse> actual = systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount);
        //then
        Assertions.assertNotNull(actual);
    }

    @Test
    void givenPlnCurrencyCodeTopCount_whenProvideMajorDifferenceBuyAskRate_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "pln";
        //when

        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount));
    }

    @Test
    void given2lettersCurrencyCodeTopCount_whenProvideMajorDifferenceBuyAskRate_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "ad";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount));
    }

    @Test
    void given4lettersCurrencyCodeTopCount_whenProvideMajorDifferenceBuyAskRate_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "test";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount));
    }

    @Test
    void givenCurrencyCodeMoreThanConditionTopCount_whenProvideMajorDifferenceBuyAskRate_thenRestErrorResponse() {
        //given
        int topCount = 256;
        String currencyCode = "aud";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount));

    }

    @Test
    void givenCurrencyCodeLessThanConditionTopCount_whenProvideMajorDifferenceBuyAskRate_thenRestErrorResponse() {
        //given
        int topCount = 0;
        String currencyCode = "aud";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideMajorDifferenceBuyAskRate(currencyCode, topCount));
    }

    ////////////////////////////////////////MinMaxAverageTests //////////////////////////////////////////////////////////////
    @Test
    void givenCurrencyCodeTopCount_whenProvideMinMaxAverageValue_thenCorrectWork() throws Exception {
        //given
        int topCount = 10;
        String currencyCode = "aud";
        //when
        ResponseEntity<BigDecimalResponses> actual = systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount);
        //then
        Assertions.assertNotNull(actual);
    }

    @Test
    void givenPlnCurrencyCodeTopCount_whenProvideMinMaxAverageValue_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "pln";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount));

    }

    @Test
    void given2lettersCurrencyCodeTopCount_whenProvideMinMaxAverageValue_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "ad";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount));

    }

    @Test
    void given4lettersCurrencyCodeTopCount_whenProvideMinMaxAverageValue_thenRestErrorResponse() {
        //given
        int topCount = 10;
        String currencyCode = "test";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount));
    }

    @Test
    void givenCurrencyCodeMoreThanConditionTopCount_whenProvideMinMaxAverageValue_thenRestErrorResponse() {
        //given
        int topCount = 256;
        String currencyCode = "aud";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount));
    }

    @Test
    void givenCurrencyCodeLessThanConditionTopCount_whenProvideMinMaxAverageValue_thenRestErrorResponse() {
        //given
        int topCount = 0;
        String currencyCode = "aud";
        //when
        //then
        Assertions.assertThrows(ProvideController.RestValidationException.class, () -> systemUnderTest.provideMinMaxAverageValue(currencyCode, topCount));

    }
}