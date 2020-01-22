package it.gabrieletondi.telldontaskkata.useCase.rejection;

import it.gabrieletondi.telldontaskkata.domain.order.CreatedOrder;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class OrderRejectionRequest {
    public int orderId;

    public OrderRejectionRequest(int orderId) {
        this.orderId = orderId;
    }

    ApproveOrderCommand toFunctionalCommand(OrderRepository orderRepository) {
        return new ApproveOrderCommand(orderId, orderRepository);
    }

    public static class ApproveOrderCommand {
        private final int orderId;
        private final OrderRepository orderRepository;

        ApproveOrderCommand(int orderId, OrderRepository orderRepository) {
            this.orderId = orderId;
            this.orderRepository = orderRepository;
        }

        void run() {
            orderRepository.findCreatedOrderById(orderId)
                    .map(CreatedOrder::reject)
                    .ifPresent(orderRepository::save);
        }

    }
}
