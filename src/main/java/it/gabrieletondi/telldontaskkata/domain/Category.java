package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.valueOf;

public class Category {
    private BigDecimal taxPercentage;

    private Category(String taxPercentage) {
        this.taxPercentage = new BigDecimal(taxPercentage);
    }

    public static Category withPercentage(String taxPercentage) {
        return new Category(taxPercentage);
    }

    BigDecimal percentageRatio() {
        return taxPercentage
                .divide(valueOf(100), taxPercentage.scale() + 2, RoundingMode.UNNECESSARY);
    }
}
