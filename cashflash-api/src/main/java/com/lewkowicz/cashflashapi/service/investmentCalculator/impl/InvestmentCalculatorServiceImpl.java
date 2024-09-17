package com.lewkowicz.cashflashapi.service.investmentCalculator.impl;

import com.lewkowicz.cashflashapi.dto.investmentCalculator.InvestmentRequest;
import com.lewkowicz.cashflashapi.dto.investmentCalculator.InvestmentResponse;
import com.lewkowicz.cashflashapi.entity.investmentCalculator.Investment;
import com.lewkowicz.cashflashapi.service.investmentCalculator.IInvestmentCalculatorService;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lewkowicz.cashflashapi.constants.investmentCalculator.InvestmentCalculatorConstants.*;

@Service
public class InvestmentCalculatorServiceImpl implements IInvestmentCalculatorService {

    @Override
    public InvestmentResponse createInvestment(InvestmentRequest investmentRequest) {

        int investmentLength = investmentRequest.getInvestmentLength();
        double investmentAmount = investmentRequest.getInvestmentAmount();
        boolean reinvest = investmentRequest.isReinvest();

        List<Investment> investments = new ArrayList<>();
        double totalSavings;
        LocalDate endDate = LocalDate.now().plusYears(investmentLength);

        for (int i = 0; i < investmentLength * 12; i++) {
            LocalDate purchaseDate = LocalDate.now().plusMonths(i);
            LocalDate maturityDate = purchaseDate.plusYears(BOND_LENGTH_YEARS);

            double futureValue = investmentAmount * Math.pow(1 + ANNUAL_INTEREST_RATE, BOND_LENGTH_YEARS);
            double profit = futureValue - investmentAmount;
            double tax = profit * TAX_RATE;
            double netProfit = profit - tax;

            investments.add(new Investment(purchaseDate, maturityDate, netProfit, false));
        }

        if (reinvest) {
            reinvestMature(investments, endDate);
        }

        totalSavings = investments.stream().mapToDouble(investment -> investment.getNetProfit() + investmentAmount).sum();

        DecimalFormat df = new DecimalFormat("#,###.00");

        double totalSavingsWithoutInvestment = investmentAmount * 12 * investmentLength;
        double difference = totalSavings - totalSavingsWithoutInvestment;

        InvestmentResponse response = new InvestmentResponse();
        response.setInvestmentDetails(investments.stream()
                .map(investment -> String.format("Investment purchased on %s will mature on %s with a net profit of %s zł. Reinvested: %s",
                        investment.getPurchaseDate(), investment.getMaturityDate(), df.format(investment.getNetProfit()), investment.isReinvested() ? "Yes" : "No"))
                .collect(Collectors.toList()));
        response.setTotalSavings(df.format(totalSavings) + " zł");
        response.setTotalSavingsWithoutInvestment(df.format(totalSavingsWithoutInvestment) + " zł");
        response.setDifference(df.format(difference) + " zł");

        return response;
    }

    @Override
    public void reinvestMature(List<Investment> investments, LocalDate endDate) {
        double leftover = 0;

        for (Investment investment : investments) {
            if (investment.getMaturityDate().isBefore(endDate)) {
                double totalAvailable = investment.getNetProfit() + leftover;
                int numberOfInvestments = (int) (totalAvailable / 100);
                double investedAmount = numberOfInvestments * 100;
                leftover = totalAvailable - investedAmount;

                double futureValue = investedAmount * Math.pow(1 + ANNUAL_INTEREST_RATE, BOND_LENGTH_YEARS);
                double profit = futureValue - investedAmount;
                double tax = profit * TAX_RATE;
                double netProfit = profit - tax;

                investment.setMaturityDate(investment.getMaturityDate().plusYears(BOND_LENGTH_YEARS));
                investment.setNetProfit(investment.getNetProfit() + netProfit);
                investment.setReinvested(true);
            }
        }

    }

}
