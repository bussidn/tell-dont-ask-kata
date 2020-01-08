package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

public class Category {
    private BigDecimal taxPercentage;

    BigDecimal getTaxPercentage() {
        return taxPercentage;
    }

    protected void setTaxPercentage(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
    }
}
