package it.gabrieletondi.telldontaskkata.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

@Builder
@EqualsAndHashCode
@ToString
public class OrderItem {

    private Product product;
    private int quantity;

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
