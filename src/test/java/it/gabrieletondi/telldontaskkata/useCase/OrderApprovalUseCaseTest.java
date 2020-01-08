package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.order.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.order.RejectedOrderCannotBeApprovedException;
import it.gabrieletondi.telldontaskkata.domain.order.ShippedOrdersCannotBeChangedException;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.useCase.approval.OrderApprovalRequest;
import it.gabrieletondi.telldontaskkata.useCase.approval.OrderApprovalUseCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static it.gabrieletondi.telldontaskkata.domain.order.Order.createOrderWithId;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderApprovalUseCaseTest {

    @Rule
    public ExpectedException ee = ExpectedException.none();

    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final OrderApprovalUseCase useCase = new OrderApprovalUseCase(orderRepository);

    @Test
    public void approvedExistingOrder() {
        int orderId = 1;
        Order initialOrder = createOrder(orderId, OrderStatus.CREATED);
        orderRepository.save(initialOrder);

        OrderApprovalRequest request = approvalRequest(orderId);

        useCase.run(request);

        final Order savedOrder = orderRepository.getById(orderId);
        assertThat(savedOrder.getStatus(), is(OrderStatus.APPROVED));
    }

    @Test
    public void rejectedExistingOrder() {
        int orderId = 2;
        Order initialOrder = createOrder(orderId, OrderStatus.CREATED);
        orderRepository.save(initialOrder);

        OrderApprovalRequest request = rejectionRequest(orderId);

        useCase.run(request);

        final Order savedOrder = orderRepository.getById(orderId);
        assertThat(savedOrder.getStatus(), is(OrderStatus.REJECTED));
    }

    @Test
    public void cannotApproveRejectedOrder() {
        int orderId = 3;
        Order initialOrder = createOrder(orderId, OrderStatus.REJECTED);
        orderRepository.addOrder(initialOrder);

        ee.expect(RejectedOrderCannotBeApprovedException.class);

        try {
            useCase.run(approvalRequest(orderId));
        } finally {
            assertThat(orderRepository.getById(orderId), is(initialOrder));
        }


    }

    @Test(expected = ApprovedOrderCannotBeRejectedException.class)
    public void cannotRejectApprovedOrder() {
        int orderId = 4;
        Order initialOrder = createOrder(orderId, OrderStatus.APPROVED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = approvalOrRejectionRequest(orderId, false);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeApproved() {
        int orderId = 5;
        Order initialOrder = createOrder(orderId, OrderStatus.SHIPPED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalRequest request = approvalOrRejectionRequest(orderId, true);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    private Order createOrder(int id, OrderStatus shipped) {
        return createOrderWithId(id)
                .status(shipped)
                .build();
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeRejected() {
        Order initialOrder = createOrderWithId(1)
                .status(OrderStatus.SHIPPED)
                .build();
        orderRepository.addOrder(initialOrder);
        boolean approved = false;

        OrderApprovalRequest request = approvalOrRejectionRequest(1, approved);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    private OrderApprovalRequest rejectionRequest(int orderId) {
        return approvalOrRejectionRequest(orderId, false);
    }

    private OrderApprovalRequest approvalRequest(int orderId) {
        return approvalOrRejectionRequest(orderId, true);
    }

    private OrderApprovalRequest approvalOrRejectionRequest(int orderId, boolean approved) {
        OrderApprovalRequest request = new OrderApprovalRequest();
        request.orderId = orderId;
        request.approved = approved;
        return request;
    }
}
