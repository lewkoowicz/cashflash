package com.lewkowicz.cashflashapi.service.investmentCalculator;

import com.lewkowicz.cashflashapi.dto.investmentCalculator.InvestmentDto;
import com.lewkowicz.cashflashapi.dto.investmentCalculator.InvestmentRequestDto;
import com.lewkowicz.cashflashapi.dto.investmentCalculator.InvestmentResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface IInvestmentCalculatorService {

    InvestmentResponseDto createInvestment(InvestmentRequestDto investmentRequest);

    void reinvestMature(List<InvestmentDto> investments, LocalDate endDate);

}
