package org.LamberM.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.LamberM.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;

public class AverageExchangeRateServiceTest implements UnitTest {
    @InjectMocks
    AverageExchangeRateService systemUnderTest;

    @Test
    void givenCodeAndLocalDate_whenGetResponse_thenResponseIsCorrect() throws JsonProcessingException {
        //given
        Currency currency = Currency.getInstance("AUD");
        LocalDate date = LocalDate.parse(("2023-04-21"));
        //when
        BigDecimal actual = systemUnderTest.getResponse(currency, date).setScale(4, RoundingMode.HALF_UP);
        //then
        BigDecimal expected = new BigDecimal("2.8094");
        Assertions.assertEquals(actual, expected);
    }
}