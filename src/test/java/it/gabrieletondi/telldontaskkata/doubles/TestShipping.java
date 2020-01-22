package it.gabrieletondi.telldontaskkata.doubles;

import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.domain.order.ShippedOrder;
import it.gabrieletondi.telldontaskkata.domain.order.SentToShippingOrder;
import it.gabrieletondi.telldontaskkata.service.Shipping;

public class TestShipping implements Shipping {
    private Order shippedOrder = null;

    public Order getShippedOrder() {
        return shippedOrder;
    }

    @Override
    public ShippedOrder ship(SentToShippingOrder order) {
        this.shippedOrder = order;
        return order.asShippedOrder();
    }
}
