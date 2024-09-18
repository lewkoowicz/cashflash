package com.lewkowicz.cashflashapi.dto.investmentCalculator;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class InvestmentResponseDto {

    private List<String> investmentDetails;

    private String totalSavings;

    private String totalSavingsWithoutInvestment;

    private String difference;

}
