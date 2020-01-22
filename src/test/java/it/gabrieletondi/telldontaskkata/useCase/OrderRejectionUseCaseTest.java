package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.order.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.domain.order.RejectedOrderCannotBeApprovedException;
import it.gabrieletondi.telldontaskkata.domain.order.ShippedOrdersCannotBeChangedException;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.useCase.approval.OrderApprovalRequest;
import it.gabrieletondi.telldontaskkata.useCase.approval.OrderApprovalUseCase;
import it.gabrieletondi.telldontaskkata.useCase.rejection.OrderRejectionRequest;
import it.gabrieletondi.telldontaskkata.useCase.rejection.OrderRejectionUseCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static it.gabrieletondi.telldontaskkata.domain.order.Order.createOrderWithId;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderRejectionUseCaseTest {

    @Rule
    public final ExpectedException ee = ExpectedException.none();

    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final OrderRejectionUseCase useCase = new OrderRejectionUseCase(orderRepository);

    @Test
    public void rejectedExistingOrder() {
        int orderId = 2;
        Order initialOrder = createOrder(orderId, OrderStatus.CREATED);
        orderRepository.save(initialOrder);

        OrderRejectionRequest request = rejectionRequest(orderId);

        useCase.run(request);

        final Order savedOrder = orderRepository.getById(orderId);
        assertThat(savedOrder.getStatus(), is(OrderStatus.REJECTED));
    }

    @Test(expected = ApprovedOrderCannotBeRejectedException.class)
    public void cannotRejectApprovedOrder() {
        int orderId = 4;
        Order initialOrder = createOrder(orderId, OrderStatus.APPROVED);
        orderRepository.addOrder(initialOrder);

        OrderRejectionRequest request = rejectionRequest(orderId);

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

        OrderRejectionRequest request = rejectionRequest(1);

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    private OrderRejectionRequest rejectionRequest(int orderId) {
        return new OrderRejectionRequest(orderId);
    }

    private OrderApprovalRequest approvalOrRejectionRequest(int orderId, boolean approved) {
        OrderApprovalRequest request = new OrderApprovalRequest();
        request.orderId = orderId;
        request.approved = approved;
        return request;
    }
}
