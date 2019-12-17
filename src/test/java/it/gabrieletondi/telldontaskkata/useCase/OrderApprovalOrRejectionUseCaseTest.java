package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderApprovalOrRejectionUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final OrderApprovalUseCase useCase = new OrderApprovalUseCase(orderRepository);

    @Test
    public void approvedExistingOrder() {
        int id = 1;
        Order initialOrder = createOrder(id, OrderStatus.CREATED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalOrRejectionRequest request = new OrderApprovalOrRejectionRequest();
        request.setOrderId(id);
        request.setApproved(true);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus(), is(OrderStatus.APPROVED));
    }

    private Order createOrder(int id, OrderStatus status) {
        return Order.builder()
                .status(status)
                .id(id)
                .build();
    }

    @Test
    public void rejectedExistingOrder() {
        Order initialOrder = createOrder(1, OrderStatus.CREATED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalOrRejectionRequest request = new OrderApprovalOrRejectionRequest();
        request.setOrderId(1);
        request.setApproved(false);

        useCase.run(request);

        final Order savedOrder = orderRepository.getSavedOrder();
        assertThat(savedOrder.getStatus(), is(OrderStatus.REJECTED));
    }

    @Test(expected = RejectedOrderCannotBeApprovedException.class)
    public void cannotApproveRejectedOrder() {
        Order initialOrder = createOrder(1, OrderStatus.REJECTED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalOrRejectionRequest request = new OrderApprovalOrRejectionRequest();
        request.setOrderId(1);
        request.setApproved(true);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ApprovedOrderCannotBeRejectedException.class)
    public void cannotRejectApprovedOrder() {
        Order initialOrder = createOrder(1, OrderStatus.APPROVED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalOrRejectionRequest request = new OrderApprovalOrRejectionRequest();
        request.setOrderId(1);
        request.setApproved(false);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeApproved() {
        Order initialOrder = createOrder(1, OrderStatus.SHIPPED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalOrRejectionRequest request = new OrderApprovalOrRejectionRequest();
        request.setOrderId(1);
        request.setApproved(true);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeRejected() {
        Order initialOrder = createOrder(1, OrderStatus.SHIPPED);
        orderRepository.addOrder(initialOrder);

        OrderApprovalOrRejectionRequest request = new OrderApprovalOrRejectionRequest();
        request.setOrderId(1);
        request.setApproved(false);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }
}
