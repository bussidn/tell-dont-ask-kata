package it.gabrieletondi.telldontaskkata.domain;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class Category {
    private String name;
    private BigDecimal taxPercentage;

    public BigDecimal getTaxPercentage() {
        return taxPercentage;
    }
}
