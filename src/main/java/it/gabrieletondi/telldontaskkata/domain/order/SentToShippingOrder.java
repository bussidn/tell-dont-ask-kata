package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;

import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.SENT_TO_SHIPPING;

public class SentToShippingOrder extends Order {
    SentToShippingOrder(int id, String currency, List<OrderItem> items) {
        super(id, SENT_TO_SHIPPING, currency, items);
    }

    public ShippedOrder asShippedOrder() {
        return statusFactory(ShippedOrder.constructorAsFunction());
    }
}
