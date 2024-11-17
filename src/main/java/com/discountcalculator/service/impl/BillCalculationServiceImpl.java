package com.discountcalculator.service.impl;

import com.discountcalculator.integration.fx.ForeignExchangeService;
import com.discountcalculator.model.BillCalculationRequest;
import com.discountcalculator.model.BillCalculationResponse;
import com.discountcalculator.model.Item;
import com.discountcalculator.service.BillCalculationService;
import com.discountcalculator.service.DiscountCalculationService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BillCalculationServiceImpl implements BillCalculationService {


    private final DiscountCalculationService discountCalculationService;
    private final ForeignExchangeService foreignExchangeService;

    @Override
    public BillCalculationResponse calculate(BillCalculationRequest calculationRequest) {

        BigDecimal netDiscountedAmount = discountCalculationService.getNetDiscountedAmount(calculationRequest);
        BigDecimal billTotal = getBillTotalAfterCcyConversion(calculationRequest);
        BigDecimal discountedAmountAfterConversion = getDiscountedAmountAfterConversion(
                calculationRequest,
                netDiscountedAmount);

        return new BillCalculationResponse(
                billTotal.subtract(discountedAmountAfterConversion),
                billTotal);
    }

    private BigDecimal getBillTotalAfterCcyConversion(BillCalculationRequest calculationRequest) {

        BigDecimal totalBillTotal = calculationRequest.getItems().stream()
                .map(Item::getPrice).reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add);

        return foreignExchangeService.currencyConversion(
                totalBillTotal,
                calculationRequest.getOriginalCurrency(),
                calculationRequest.getTargetCurrency());
    }

    private BigDecimal getDiscountedAmountAfterConversion(
            BillCalculationRequest calculationRequest,
            BigDecimal discountedAmount) {

        return foreignExchangeService.currencyConversion(
                discountedAmount,
                calculationRequest.getOriginalCurrency(),
                calculationRequest.getTargetCurrency());
    }
}


