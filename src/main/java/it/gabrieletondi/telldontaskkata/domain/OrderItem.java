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
        final BigDecimal unitaryTaxedAmount = product.getPrice().add(unitaryTax()).setScale(2, HALF_UP);
        return unitaryTaxedAmount.multiply(BigDecimal.valueOf(quantity)).setScale(2, HALF_UP);
    }

    private BigDecimal unitaryTax() {
        return product.getPrice().divide(valueOf(100)).multiply(product.getCategory().getTaxPercentage()).setScale(2, HALF_UP);
    }

    public BigDecimal getTax() {
        return unitaryTax().multiply(BigDecimal.valueOf(quantity));
    }
}
