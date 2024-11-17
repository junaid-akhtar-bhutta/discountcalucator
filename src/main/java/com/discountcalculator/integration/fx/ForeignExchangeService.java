package com.discountcalculator.integration.fx;

import java.math.BigDecimal;
import java.util.List;

public interface ForeignExchangeService {


    FxRate getFxRatesByBaseCurrency(String currency);


    public default BigDecimal currencyConversion(BigDecimal amount, String baseCurrency, String targetCurrency) {

        FxRate fxRate = getFxRatesByBaseCurrency(baseCurrency);
        BigDecimal conversionRate = fxRate.getConversionRates().get(targetCurrency);
        if (conversionRate == null) {
            throw new IllegalStateException("Currency conversion not available in target currency");
        }
        return amount.multiply(conversionRate);
    }
}
