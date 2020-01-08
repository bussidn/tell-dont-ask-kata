package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return quantity == orderItem.quantity &&
                product.equals(orderItem.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity);
    }
}
