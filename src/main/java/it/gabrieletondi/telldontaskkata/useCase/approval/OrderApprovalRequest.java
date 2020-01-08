package it.gabrieletondi.telldontaskkata.useCase.approval;

import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class OrderApprovalRequest {
    public int orderId;
    public boolean approved;

    ApproveOrderCommand toFunctionalCommand(OrderRepository orderRepository) {
        return new ApproveOrderCommand(orderId, approved, orderRepository);
    }

    public static class ApproveOrderCommand {
        private final int orderId;
        private final boolean approved;
        private final OrderRepository orderRepository;

        ApproveOrderCommand(int orderId, boolean approved, OrderRepository orderRepository) {
            this.orderId = orderId;
            this.approved = approved;
            this.orderRepository = orderRepository;
        }

        void run() {
            final Order order = orderRepository.getById(orderId);
            approveOrReject(order);
            orderRepository.save(order);
        }

        private void approveOrReject(Order order) {
            if (approved) order.approve();
            else order.reject();
        }

    }
}
