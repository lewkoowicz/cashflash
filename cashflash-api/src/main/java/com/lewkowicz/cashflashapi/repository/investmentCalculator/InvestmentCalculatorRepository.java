package com.lewkowicz.cashflashapi.repository.investmentCalculator;

import com.lewkowicz.cashflashapi.entity.investmentCalculator.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentCalculatorRepository extends JpaRepository<Investment, Long> {}
