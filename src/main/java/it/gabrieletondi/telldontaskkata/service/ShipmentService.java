package it.gabrieletondi.telldontaskkata.service;

import it.gabrieletondi.telldontaskkata.domain.order.Order;

public interface ShipmentService {
    void ship(Order order);
}
