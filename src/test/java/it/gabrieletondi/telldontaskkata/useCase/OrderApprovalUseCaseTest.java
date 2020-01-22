package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.order.CreatedOrder;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.useCase.approval.OrderApprovalRequest;
import it.gabrieletondi.telldontaskkata.useCase.approval.OrderApprovalUseCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

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
        CreatedOrder initialOrder = new CreatedOrder(orderId, "EUR", new ArrayList<>());
        orderRepository.save(initialOrder);

        OrderApprovalRequest request = approvalRequest(orderId);

        useCase.run(request);

        final Order savedOrder = orderRepository.getById(orderId);
        assertThat(savedOrder.getStatus(), is(OrderStatus.APPROVED));
    }

    private OrderApprovalRequest approvalRequest(int orderId) {
        return new OrderApprovalRequest(orderId);
    }

}
