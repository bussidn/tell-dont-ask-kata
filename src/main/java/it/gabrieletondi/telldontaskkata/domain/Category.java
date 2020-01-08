package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.valueOf;

public class Category {
    private BigDecimal taxPercentage;

    private Category(BigDecimal taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public static Category withPercentage(String taxPercentage) {
        return new Category(new BigDecimal(taxPercentage));
    }

    BigDecimal percentageRatio() {
        return taxPercentage
                .divide(valueOf(100), taxPercentage.scale() + 2, RoundingMode.UNNECESSARY);
    }
}
