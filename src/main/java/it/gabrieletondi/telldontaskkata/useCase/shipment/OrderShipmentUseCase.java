package it.gabrieletondi.telldontaskkata.useCase.shipment;

import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.service.Shipping;

public class OrderShipmentUseCase {
    private final OrderRepository orderRepository;
    private final Shipping shipping;

    public OrderShipmentUseCase(OrderRepository orderRepository, Shipping shipping) {
        this.orderRepository = orderRepository;
        this.shipping = shipping;
    }

    public void run(OrderShipmentRequest request) {
        request.toFunctionalShipCommand(orderRepository, shipping)
                .run();
    }

}
