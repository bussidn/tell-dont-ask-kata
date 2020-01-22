package it.gabrieletondi.telldontaskkata.useCase.shipment;

import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.service.Shipping;

public class OrderShipmentRequest {
    public int orderId;

    ShipOrderCommand toFunctionalShipCommand(OrderRepository orderRepository, Shipping shipping) {
        return new ShipOrderCommand(orderId, orderRepository, shipping);
    }

    public class ShipOrderCommand {

        private final int orderId;
        private final OrderRepository orderRepository;
        private final Shipping shipping;

        ShipOrderCommand(int orderId, OrderRepository orderRepository, Shipping shipping) {
            this.orderId = orderId;
            this.orderRepository = orderRepository;
            this.shipping = shipping;
        }

        void run() {
            final Order order = orderRepository.getById(orderId);
            orderRepository.save(order.shipWith(shipping));
        }
    }
}
