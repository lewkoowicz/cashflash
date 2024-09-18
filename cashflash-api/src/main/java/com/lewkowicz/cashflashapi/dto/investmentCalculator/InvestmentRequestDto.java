package com.lewkowicz.cashflashapi.dto.investmentCalculator;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InvestmentRequestDto {

    private double investmentAmount;

    private int investmentLength;

    private boolean reinvest;

}
