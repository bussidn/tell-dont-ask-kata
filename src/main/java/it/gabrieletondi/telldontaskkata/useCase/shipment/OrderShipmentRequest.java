package it.gabrieletondi.telldontaskkata.useCase.shipment;

import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.service.Shipping;

public class OrderShipmentRequest {
    public int orderId;

    public OrderShipmentRequest(int orderId) {
        this.orderId = orderId;
    }

    ShipOrderCommand toFunctionalShipCommand(OrderRepository orderRepository, Shipping shipping) {
        return new ShipOrderCommand(orderId, orderRepository, shipping);
    }

    public static class ShipOrderCommand {

        private final int orderId;
        private final OrderRepository orderRepository;
        private final Shipping shipping;

        ShipOrderCommand(int orderId, OrderRepository orderRepository, Shipping shipping) {
            this.orderId = orderId;
            this.orderRepository = orderRepository;
            this.shipping = shipping;
        }

        void run() {
            orderRepository.findApprovedOrderById(orderId)
                    .map(order -> order.shipWith(shipping))
                    .ifPresent(orderRepository::save);
        }
    }
}
