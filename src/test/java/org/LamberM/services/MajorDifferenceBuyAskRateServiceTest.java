package org.LamberM.services;

import org.LamberM.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class MajorDifferenceBuyAskRateServiceTest implements UnitTest {
    @InjectMocks
    MajorDifferenceBuyAskRateService systemUnderTest;

    @Test
    void givenCodeAndTopCount_whenGetResponse_thenResponseIsCorrect() {
        //given
        Currency currency = Currency.getInstance("AUD");
        int topCount = 10;
        //when
        BigDecimal actual = systemUnderTest.getResponse(currency, topCount).setScale(4, RoundingMode.HALF_UP);
        //then
        BigDecimal expected = new BigDecimal("0.0570");
        Assertions.assertEquals(expected, actual);
    }

}