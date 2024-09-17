package com.lewkowicz.cashflashapi.entity.investmentCalculator;

import com.lewkowicz.cashflashapi.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor
public class Investment extends BaseEntity {

    public Investment(LocalDate purchaseDate, LocalDate maturityDate, double netProfit, boolean reinvested) {
        this.purchaseDate = purchaseDate;
        this.maturityDate = maturityDate;
        this.netProfit = netProfit;
        this.reinvested = reinvested;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long investmentId;

    private LocalDate purchaseDate;

    private LocalDate maturityDate;

    private double netProfit;

    private boolean reinvested;

}
