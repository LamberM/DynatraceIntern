package org.LamberM.utils;

import java.math.BigDecimal;

public class ExchangeRateResponse {
    private final BigDecimal value;

    public ExchangeRateResponse(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getAverage() {
        return value;
    }

}
