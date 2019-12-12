package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.service.ShipmentService;
import it.gabrieletondi.telldontaskkata.useCase.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String currency = "EUR";
    private List<OrderItem> items = new ArrayList<>();
    private OrderStatus status = OrderStatus.CREATED;
    private int id;

    public BigDecimal getTotal() {
        return items.stream()
                .map(OrderItem::getTaxedAmount)
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
                .map(OrderItem::getTax)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void approveOrReject(boolean isApproved) {
        if (status.equals(OrderStatus.SHIPPED)) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if (isApproved && status.equals(OrderStatus.REJECTED)) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        if (!isApproved && status.equals(OrderStatus.APPROVED)) {
            throw new ApprovedOrderCannotBeRejectedException();
        }
        status = isApproved ? OrderStatus.APPROVED : OrderStatus.REJECTED;
    }

    public int getId() {
        return id;
    }

    public void addItem(OrderItem orderItem) {
        items.add(orderItem);
    }

    public void ship(ShipmentService shipmentService) {
        if (status.equals(CREATED) || status.equals(REJECTED)) {
            throw new OrderCannotBeShippedException();
        }

        if (status.equals(SHIPPED)) {
            throw new OrderCannotBeShippedTwiceException();
        }

        shipmentService.ship(this);

        status = OrderStatus.SHIPPED;
    }
}
