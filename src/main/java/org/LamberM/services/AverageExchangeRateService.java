package org.LamberM.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
@Service
public class AverageExchangeRateService {

    public BigDecimal getResponse(Currency currencyCode, LocalDate date) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String formattedDate = DateTimeFormatter.ISO_LOCAL_DATE.format(date);

        String url= ("https://api.nbp.pl/api/exchangerates/rates/a/"+currencyCode+"/"+formattedDate+"/?format=json");
        String json = restTemplate.getForObject(url,String.class);
        JsonNode rootNode = objectMapper.readTree(json);
        JsonNode midNode = rootNode.at("/rates/0/mid");
        return midNode.decimalValue();
    }
}
