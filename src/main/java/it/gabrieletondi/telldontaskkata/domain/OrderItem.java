package it.gabrieletondi.telldontaskkata.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

@Builder
@ToString
@EqualsAndHashCode
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderItem {

    @NonNull Product product;
    int quantity;

    public BigDecimal getTaxedAmount() {
        return multiplyByQuantity(product.unitaryTaxedAmount());
    }

    public BigDecimal getTax() {
        return multiplyByQuantity(product.unitaryTax());
    }

    private BigDecimal multiplyByQuantity(BigDecimal unitaryTaxedAmount) {
        return unitaryTaxedAmount.multiply(BigDecimal.valueOf(quantity)).setScale(2, HALF_UP);
    }
}
