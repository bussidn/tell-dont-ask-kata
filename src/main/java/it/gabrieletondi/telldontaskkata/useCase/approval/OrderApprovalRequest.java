package it.gabrieletondi.telldontaskkata.useCase.approval;

import it.gabrieletondi.telldontaskkata.domain.order.ApprovedOrder;
import it.gabrieletondi.telldontaskkata.domain.order.CreatedOrder;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

import java.util.Optional;

public class OrderApprovalRequest {
    public int orderId;

    public OrderApprovalRequest(int orderId) {
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
                    .map(CreatedOrder::approve)
                    .ifPresent(orderRepository::save);
        }
    }
}
