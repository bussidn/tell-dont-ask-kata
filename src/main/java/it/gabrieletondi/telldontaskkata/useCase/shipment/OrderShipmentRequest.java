package it.gabrieletondi.telldontaskkata.useCase.shipment;

import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.service.ShipmentService;

public class OrderShipmentRequest {
    public int orderId;

    ShipOrderCommand toFunctionalShipCommand(OrderRepository orderRepository, ShipmentService shipmentService) {
        return new ShipOrderCommand(orderId, orderRepository, shipmentService);
    }

    public class ShipOrderCommand {

        private final int orderId;
        private final OrderRepository orderRepository;
        private final ShipmentService shipmentService;

        ShipOrderCommand(int orderId, OrderRepository orderRepository, ShipmentService shipmentService) {
            this.orderId = orderId;
            this.orderRepository = orderRepository;
            this.shipmentService = shipmentService;
        }

        void run() {
            final Order order = orderRepository.getById(orderId);
            order.shipWith(shipmentService);
            orderRepository.save(order);
        }
    }
}
