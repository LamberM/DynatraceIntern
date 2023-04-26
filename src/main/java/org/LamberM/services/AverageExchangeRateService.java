package org.LamberM.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.LamberM.utils.ExchangeRateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;

@Service
public class AverageExchangeRateService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public AverageExchangeRateService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public ExchangeRateResponse calculateAverageExchangeRate(Currency currency, LocalDate date) throws JsonProcessingException {
        String formattedDate = DateTimeFormatter.ISO_LOCAL_DATE.format(date);

        String url = ("https://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/" + formattedDate + "/?format=json");
        String json = restTemplate.getForObject(url, String.class);
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode midNode = rootNode.at("/rates/0/mid");
        return new ExchangeRateResponse(midNode.decimalValue());
    }
}
