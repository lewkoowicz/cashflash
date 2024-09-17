package com.lewkowicz.cashflashapi.service.investmentCalculator;

import com.lewkowicz.cashflashapi.dto.investmentCalculator.InvestmentRequest;
import com.lewkowicz.cashflashapi.dto.investmentCalculator.InvestmentResponse;
import com.lewkowicz.cashflashapi.entity.investmentCalculator.Investment;

import java.time.LocalDate;
import java.util.List;

public interface IInvestmentCalculatorService {

    InvestmentResponse createInvestment(InvestmentRequest investmentRequest);

    void reinvestMature(List<Investment> investments, LocalDate endDate);

}
