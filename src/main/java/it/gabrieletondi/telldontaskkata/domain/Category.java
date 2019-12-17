package it.gabrieletondi.telldontaskkata.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.valueOf;

@Builder
@EqualsAndHashCode
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Category {

    @NonNull String name;
    @NonNull BigDecimal taxPercentage;

    BigDecimal taxRate() {
        return taxPercentage.divide(valueOf(100), taxPercentage.scale() + 2, RoundingMode.UNNECESSARY);
    }
}
