package org.LamberM.services;

import org.LamberM.UnitTest;
import org.LamberM.utils.BigDecimalResponses;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class MinMaxAverageValueServiceTest implements UnitTest {
    @InjectMocks
    MinMaxAverageValueService systemUnderTest;

    @Test
    void givenCodeAndTopCount_whenGetResponse_thenResponseIsCorrect() {
        //given
        Currency currency = Currency.getInstance("AUD");
        int topCount = 10;
        //when
        BigDecimalResponses actual = systemUnderTest.calculateMinMaxAverageValue(currency, topCount);
        //then
        BigDecimal expectedMin = new BigDecimal("2.7380");
        BigDecimal expectedMax = new BigDecimal("2.8743");
        Assertions.assertEquals(expectedMin, actual.min().setScale(4, RoundingMode.HALF_UP));
        Assertions.assertEquals(expectedMax, actual.max().setScale(4, RoundingMode.HALF_UP));
    }
}