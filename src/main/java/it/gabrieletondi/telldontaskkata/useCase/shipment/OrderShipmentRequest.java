package it.gabrieletondi.telldontaskkata.useCase.shipment;

import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.service.ShipmentService;

public class OrderShipmentRequest {
    private int orderId;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    FunctionalOrderShipCommand toFunctionalShipCommand(OrderRepository orderRepository, ShipmentService shipmentService) {
        return new FunctionalOrderShipCommand(orderId, orderRepository, shipmentService);
    }

    public class FunctionalOrderShipCommand {

        private final int orderId;
        private final OrderRepository orderRepository;
        private final ShipmentService shipmentService;

        FunctionalOrderShipCommand(int orderId, OrderRepository orderRepository, ShipmentService shipmentService) {
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
