package it.gabrieletondi.telldontaskkata.domain;

import lombok.Builder;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

@Builder
public class OrderItem {

    private Product product;
    private int quantity;

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTaxedAmount() {
        return multiplyByQuantity(product.getUnitaryTaxedAmount());
    }

    public BigDecimal getTax() {
        return multiplyByQuantity(product.unitaryTax());
    }

    private BigDecimal multiplyByQuantity(BigDecimal unitaryTaxedAmount) {
        return unitaryTaxedAmount.multiply(BigDecimal.valueOf(quantity)).setScale(2, HALF_UP);
    }
}
