package com.discountcalculator.integration.fx;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ExchangeRateApi implements ForeignExchangeService {


    @Value("${fx-service.exchange-rate-api.base-url}")
    private String baseUrl;

    @Value("${fx-service.exchange-rate-api.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Override
    public FxRate getFxRatesByBaseCurrency(String baseCurrency) {

        ExchangeRateApiResponse apiResponse = restTemplate.getForObject(
                baseUrl + apiKey + "/latest/" + baseCurrency,
                ExchangeRateApiResponse.class);

        return new FxRate(baseCurrency, apiResponse.getConversionRates());
    }
}
