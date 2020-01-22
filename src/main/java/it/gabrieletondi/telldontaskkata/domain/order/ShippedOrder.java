package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;

import java.util.List;

public class ShippedOrder extends Order {
    ShippedOrder(int id, OrderStatus status, String currency, List<OrderItem> items) {
        super(id, status, currency, items);
    }
}
