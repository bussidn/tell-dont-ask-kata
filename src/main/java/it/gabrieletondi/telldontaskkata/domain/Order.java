package it.gabrieletondi.telldontaskkata.domain;

import it.gabrieletondi.telldontaskkata.service.ShipmentService;
import it.gabrieletondi.telldontaskkata.useCase.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.*;

@With
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Order {

    private static final String EURO = "EUR";

    @Builder.Default
    @NonNull String currency = EURO;

    @Singular
    @NonNull List<OrderItem> items;

    @Builder.Default
    @NonNull OrderStatus status = OrderStatus.CREATED;

    int id;

    public BigDecimal getTotal() {
        return items.stream()
                .map(OrderItem::getTaxedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    public Order reject() {
        if (status.equals(OrderStatus.SHIPPED)) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if (status.equals(OrderStatus.APPROVED)) {
            throw new ApprovedOrderCannotBeRejectedException();
        }

        return this.withStatus(REJECTED);
    }

    public Order approve() {
        if (status.equals(OrderStatus.SHIPPED)) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if (status.equals(OrderStatus.REJECTED)) {
            throw new RejectedOrderCannotBeApprovedException();
        }

        return this.withStatus(APPROVED);
    }

    public int getId() {
        return id;
    }

    public Order ship(ShipmentService shipmentService) {
        if (status.equals(CREATED) || status.equals(REJECTED)) {
            throw new OrderCannotBeShippedException();
        }

        if (status.equals(SHIPPED)) {
            throw new OrderCannotBeShippedTwiceException();
        }

        shipmentService.ship(this);

        return this.withStatus(SHIPPED);
    }
}
