package org.LamberM.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.LamberM.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Currency;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AverageExchangeRateServiceTest implements UnitTest {
    @InjectMocks
    AverageExchangeRateService systemUnderTest;
    @Mock
    RestTemplate restTemplateMock;
    @Mock
    ObjectMapper objectMapperMock;
    @Test
    void givenCodeAndLocalDate_whenGetResponse_thenResponseIsCorrect() throws JsonProcessingException {
        //given
        Currency currency = Currency.getInstance("AUD");
        LocalDate date = LocalDate.parse(("2023-04-21"));
        String formattedDate=date.toString();
        String currencyCode = currency.toString();

        when(restTemplateMock.getForObject("https://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/" + formattedDate + "/?format=json".formatted(currencyCode,formattedDate), String.class)).thenReturn("json");
        JsonNode rootNodeMock= mock(JsonNode.class);
        when(objectMapperMock.readTree("json")).thenReturn(rootNodeMock);
        JsonNode midValueMock = mock(JsonNode.class);
        when(rootNodeMock.at("/rates/0/mid")).thenReturn(midValueMock);
        when(midValueMock.decimalValue()).thenReturn(BigDecimal.valueOf(2.8094));
        //when
        BigDecimal actual = systemUnderTest.calculateAverageExchangeRate(currency, date).getAverage().setScale(4, RoundingMode.HALF_UP);
        //then
        BigDecimal expected = new BigDecimal("2.8094");
        Assertions.assertEquals(actual, expected);
    }
}