package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.service.ShipmentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

public class Order {
    private final int id;
    private final String currency;
    private OrderStatus status;
    private final List<OrderItem> items;

    Order(int id, OrderStatus status, String currency, List<OrderItem> items) {
        this.id = id;
        this.status = status;
        this.currency = currency;
        this.items = items;
    }

    <T extends Order> Function<OrderStatus, T> statusFactory(Function<Integer, Function<OrderStatus, Function<String, Function<List<OrderItem>, T>>>> constructor) {
        return status -> constructor.apply(id).apply(status).apply(currency).apply(items);
    }

    public ToBeShippedOrder sentToShippingService() {
        return new ToBeShippedOrder(id, SENT_TO_SHIPPING_SERVICE, currency, items);
    }

    public static Order.Builder createOrderWithId(int orderId) {
        return new Order.Builder(orderId);
    }

    public static Order initializeOrderWith(String currency, int orderId) {
        return createOrderWithId(orderId)
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

    private boolean isShipped() {
        return status.equals(OrderStatus.SHIPPED);
    }

    private void throwIfShipped() {
        if (isShipped()) {
            throw new ShippedOrdersCannotBeChangedException();
        }
    }

    private boolean isRejected() {
        return status.equals(OrderStatus.REJECTED);
    }

    private boolean isApproved() {
        return status.equals(OrderStatus.APPROVED);
    }

    public Order approve() {
        throwIfShipped();
        if (isRejected()) {
            throw new RejectedOrderCannotBeApprovedException();
        }
        this.status = OrderStatus.APPROVED;
        return this;
    }

    public Order reject() {
        throwIfShipped();
        if (isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }
        this.status = OrderStatus.REJECTED;
        return this;
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

    public ShippedOrder shipWith(ShipmentService shipmentService) {
        if (isCreated() || isRejected()) {
            throw new OrderCannotBeShippedException();
        }

        if (isShipped()) {
            throw new OrderCannotBeShippedTwiceException();
        }

        return shipmentService.ship(this.sentToShippingService());
    }

    private boolean isCreated() {
        return status.equals(CREATED);
    }

    public Order add(OrderItem orderItem) {
        items.add(orderItem);
        return this;
    }

    public static class Builder {
        private final int id;
        private OrderStatus status = CREATED;
        private String currency;
        private final List<OrderItem> items = new ArrayList<>();

        Builder(int id) {
            this.id = id;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Order build() {
            return new Order(id, status, currency, items);
        }
    }
}
