package it.gabrieletondi.telldontaskkata.doubles;

import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.domain.order.ShippedOrder;
import it.gabrieletondi.telldontaskkata.service.ShipmentService;

public class TestShipmentService implements ShipmentService {
    private Order shippedOrder = null;

    public Order getShippedOrder() {
        return shippedOrder;
    }

    @Override
    public ShippedOrder ship(Order order) {
        this.shippedOrder = order;
        return order.asShippedOrder();
    }
}
