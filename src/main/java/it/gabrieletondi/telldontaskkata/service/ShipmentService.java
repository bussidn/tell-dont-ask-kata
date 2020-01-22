package it.gabrieletondi.telldontaskkata.service;

import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.domain.order.ShippedOrder;

public interface ShipmentService {
    ShippedOrder ship(Order order);
}
