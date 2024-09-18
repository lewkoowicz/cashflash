package com.lewkowicz.cashflashapi.dto.investmentCalculator;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class InvestmentDto {

    private LocalDate purchaseDate;

    private LocalDate maturityDate;

    private double netProfit;

    private boolean reinvested;

}
