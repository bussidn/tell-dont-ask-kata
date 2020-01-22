package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.order.*;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.useCase.rejection.OrderRejectionRequest;
import it.gabrieletondi.telldontaskkata.useCase.rejection.OrderRejectionUseCase;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

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
        CreatedOrder initialOrder = new CreatedOrder(orderId, "EUR", new ArrayList<>());
        orderRepository.addOrder(initialOrder);

        OrderRejectionRequest request = rejectionRequest(orderId);

        useCase.run(request);

        final Order savedOrder = orderRepository.getById(orderId);
        assertThat(savedOrder.getStatus(), is(OrderStatus.REJECTED));
    }

    private OrderRejectionRequest rejectionRequest(int orderId) {
        return new OrderRejectionRequest(orderId);
    }

}
