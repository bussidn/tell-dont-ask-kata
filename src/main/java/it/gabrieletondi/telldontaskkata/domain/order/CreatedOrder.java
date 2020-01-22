package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;

import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.CREATED;

public class CreatedOrder extends Order {
    public CreatedOrder(int id, String currency, List<OrderItem> items) {
        super(id, CREATED, currency, items);
    }
}
