package it.gabrieletondi.telldontaskkata.domain;

import lombok.Builder;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
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
        final BigDecimal unitaryTaxedAmount = product.getPrice().add(product.unitaryTax()).setScale(2, HALF_UP);
        return unitaryTaxedAmount.multiply(BigDecimal.valueOf(quantity)).setScale(2, HALF_UP);
    }

    public BigDecimal getTax() {
        return product.unitaryTax().multiply(BigDecimal.valueOf(quantity));
    }
}
