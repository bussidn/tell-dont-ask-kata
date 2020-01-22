package it.gabrieletondi.telldontaskkata.useCase.rejection;

import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class OrderRejectionUseCase {
    private final OrderRepository orderRepository;

    public OrderRejectionUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void run(OrderRejectionRequest request) {
        request.toFunctionalCommand(orderRepository)
                .run();
    }

}
