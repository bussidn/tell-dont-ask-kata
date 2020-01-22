package it.gabrieletondi.telldontaskkata.service;

import it.gabrieletondi.telldontaskkata.domain.order.ShippedOrder;
import it.gabrieletondi.telldontaskkata.domain.order.SentToShippingOrder;

public interface Shipping {
    ShippedOrder ship(SentToShippingOrder order);
}
