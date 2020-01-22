package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
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
    public final ExpectedException ee = ExpectedException.none();

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

    @Test(expected = ShippedOrdersCannotBeChangedException.class)
    public void shippedOrdersCannotBeApproved() {
        int orderId = 5;
        Order initialOrder = createOrder(orderId, OrderStatus.SHIPPED);
        orderRepository.addOrder(initialOrder);
        OrderApprovalRequest approvalRequest = approvalRequest(orderId);


        useCase.run(approvalRequest);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
    }

    private Order createOrder(int id, OrderStatus shipped) {
        return createOrderWithId(id)
                .status(shipped)
                .build();
    }

    private OrderApprovalRequest approvalRequest(int orderId) {
        return new OrderApprovalRequest(orderId);
    }

}
