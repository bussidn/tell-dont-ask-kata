package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.service.Shipping;

import java.util.List;

public class ApprovedOrder extends Order {
    ApprovedOrder(int id, OrderStatus status, String currency, List<OrderItem> items) {
        super(id, status, currency, items);
    }

    public ShippedOrder shipWith(Shipping shipping) {
        return shipping.ship(this.sentToShippingService());
    }
}
