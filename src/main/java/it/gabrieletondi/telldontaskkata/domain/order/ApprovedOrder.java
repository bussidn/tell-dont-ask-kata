package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.service.Shipping;

import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.APPROVED;

public class ApprovedOrder extends Order {
    public ApprovedOrder(int id, String currency, List<OrderItem> items) {
        super(id, APPROVED, currency, items);
    }

    public ShippedOrder shipWith(Shipping shipping) {
        return shipping.ship(this.sentToShippingService());
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
