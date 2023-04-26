package org.LamberM.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.LamberM.utils.ExchangeRateResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;


@Service
public class MajorDifferenceBuyAskRateService {

    public ExchangeRateResponse calculateMajorDifferenceBuyAskRate(Currency currencyCode, int topCount) {
        RestTemplate restTemplate = new RestTemplate();
        double difference;
        double maxDifference = 0;
        String url = ("https://api.nbp.pl/api/exchangerates/rates/c/" + currencyCode + "/last" + "/" + topCount + "/?format=json");
        JsonNode jsonNode = restTemplate.getForObject(url, JsonNode.class);

        List<Double> minRates = new ArrayList<>();
        List<Double> maxRates = new ArrayList<>();

        JsonNode ratesNode = jsonNode.path("rates");
        for (Iterator<JsonNode> it = ratesNode.elements(); it.hasNext(); ) {
            JsonNode rateNode = it.next();
            Double ask = rateNode.path("ask").asDouble();
            Double bid = rateNode.path("bid").asDouble();
            maxRates.add(ask);
            minRates.add(bid);
        }
        for (int i = 0; i < minRates.size(); i++) {
            difference = maxRates.get(i) - minRates.get(i);
            if (difference >= maxDifference) {
                maxDifference = difference;
            }
        }
        return new ExchangeRateResponse(BigDecimal.valueOf(maxDifference));
    }
}
