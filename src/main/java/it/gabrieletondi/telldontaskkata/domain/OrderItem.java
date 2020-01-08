package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_UP;

public class OrderItem {
    private final Product product;
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal taxedAmount() {
        return multiplyByQuantity(product.unitaryTaxedAmount());
    }

    public BigDecimal tax() {
        return multiplyByQuantity(product.unitaryTax());
    }

    private BigDecimal multiplyByQuantity(BigDecimal bigDecimal) {
        return bigDecimal
                .multiply(BigDecimal.valueOf(quantity)).setScale(2, HALF_UP);
    }
}
