package com.lewkowicz.cashflashapi.controller.investmentCalculator;

import com.lewkowicz.cashflashapi.dto.investmentCalculator.InvestmentRequest;
import com.lewkowicz.cashflashapi.dto.investmentCalculator.InvestmentResponse;
import com.lewkowicz.cashflashapi.service.investmentCalculator.IInvestmentCalculatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class InvestmentCalculatorController {

    private IInvestmentCalculatorService investmentCalculatorService;

    @PostMapping("/create")
    public ResponseEntity<InvestmentResponse> createInvestment(@RequestBody InvestmentRequest investmentRequest) {
        InvestmentResponse response = investmentCalculatorService.createInvestment(investmentRequest);
        return ResponseEntity.ok(response);
    }

}
