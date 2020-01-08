package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.service.ShipmentService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.CREATED;

public class Order {
    private String currency;
    private List<OrderItem> items;
    private OrderStatus status;
    private int id;

    public BigDecimal getTotal() {
        return items.stream()
                .map(OrderItem::taxedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public BigDecimal getTax() {
        return items.stream()
                .map(OrderItem::tax)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void approve() {
        throwIfShipped();
        if (isRejected()) {
            throw new RejectedOrderCannotBeApprovedException();
        }
        setStatus(OrderStatus.APPROVED);
    }

    public void reject() {
        throwIfShipped();
        if (isApproved()) {
            throw new ApprovedOrderCannotBeRejectedException();
        }
        setStatus(OrderStatus.REJECTED);
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

    public void shipWith(ShipmentService shipmentService) {
        if (isCreated() || isRejected()) {
            throw new OrderCannotBeShippedException();
        }

        if (isShipped()) {
            throw new OrderCannotBeShippedTwiceException();
        }

        shipmentService.ship(this);

        setStatus(OrderStatus.SHIPPED);
    }

    private boolean isCreated() {
        return status.equals(CREATED);
    }
}
