package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

class OrderApprovalUseCase {
    private final OrderRepository orderRepository;

    OrderApprovalUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    final void run(OrderApprovalRequest request) {
        final Order order = orderRepository.getById(request.getOrderId());
        final Order updatedOrder = request.isApproved() ?
                order.approve() :
                order.reject();
        orderRepository.save(updatedOrder);
    }
}
