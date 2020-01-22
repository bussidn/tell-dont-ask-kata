package it.gabrieletondi.telldontaskkata.service;

import it.gabrieletondi.telldontaskkata.domain.order.ShippedOrder;
import it.gabrieletondi.telldontaskkata.domain.order.ToBeShippedOrder;

public interface ShipmentService {
    ShippedOrder ship(ToBeShippedOrder order);
}
