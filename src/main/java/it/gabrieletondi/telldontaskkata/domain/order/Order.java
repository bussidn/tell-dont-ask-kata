package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class Order {
    private final int id;
    private final String currency;
    private final OrderStatus status;
    private final List<OrderItem> items;

    Order(int id, OrderStatus status, String currency, List<OrderItem> items) {
        this.id = id;
        this.status = status;
        this.currency = currency;
        this.items = items;
    }

    <T extends Order> T orderFactory(Function<Integer, Function<String, Function<List<OrderItem>, T>>> constructor) {
        return constructor.apply(id).apply(currency).apply(items);
    }

    public SentToShippingOrder sentToShippingService() {
        return new SentToShippingOrder(id, currency, items);
    }

    public static CreatedOrder createOrderWith(String currency, int orderId) {
        return new Builder(orderId)
                .currency(currency)
                .build();
    }

    public BigDecimal getTotal() {
        return items.stream()
                .map(OrderItem::taxedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getCurrency() {
        return currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTax() {
        return items.stream()
                .map(OrderItem::tax)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Order add(OrderItem orderItem) {
        items.add(orderItem);
        return this;
    }

    public static class Builder {
        private final int id;
        private String currency;
        private final List<OrderItem> items = new ArrayList<>();

        Builder(int id) {
            this.id = id;
        }

        Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public CreatedOrder build() {
            return new CreatedOrder(id, currency, items);
        }
    }
}
