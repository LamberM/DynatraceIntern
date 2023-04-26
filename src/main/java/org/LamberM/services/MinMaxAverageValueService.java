package org.LamberM.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.LamberM.utils.BigDecimalResponses;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
public class MinMaxAverageValueService {

    public BigDecimalResponses calculateMinMaxAverageValue(Currency currency, int topCount) {
        RestTemplate restTemplate = new RestTemplate();

        String url = ("https://api.nbp.pl/api/exchangerates/rates/c/" + currency + "/last" + "/" + topCount + "/?format=json");
        JsonNode jsonNode = restTemplate.getForObject(url, JsonNode.class);

        List<BigDecimal> minRates = new ArrayList<>();
        List<BigDecimal> maxRates = new ArrayList<>();

        JsonNode ratesNode = jsonNode.path("rates");
        for (Iterator<JsonNode> it = ratesNode.elements(); it.hasNext(); ) {
            JsonNode rateNode = it.next();
            BigDecimal bid = rateNode.path("bid").decimalValue();
            minRates.add(bid);
            BigDecimal ask = rateNode.path("ask").decimalValue();
            maxRates.add(ask);
        }
        return new BigDecimalResponses(Collections.min(minRates), Collections.max(maxRates));
    }
}
