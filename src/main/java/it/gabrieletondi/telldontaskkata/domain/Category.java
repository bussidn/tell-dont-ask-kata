package it.gabrieletondi.telldontaskkata.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.valueOf;

@Builder
@EqualsAndHashCode
@ToString
public class Category {

    private String name;
    private BigDecimal taxPercentage;

    BigDecimal taxRate() {
        return taxPercentage.divide(valueOf(100), taxPercentage.scale() + 2, RoundingMode.UNNECESSARY);
    }
}
