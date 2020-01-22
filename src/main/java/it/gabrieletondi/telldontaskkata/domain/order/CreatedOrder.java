package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;

import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.CREATED;

public class CreatedOrder extends Order {
    public CreatedOrder(int id, String currency, List<OrderItem> items) {
        super(id, CREATED, currency, items);
    }

    public ApprovedOrder approve() {
        return orderFactory(ApprovedOrder.constructorAsFunction());
    }

    public RejectedOrder reject() {
        return orderFactory(RejectedOrder.constructorAsFunction());
    }
}
