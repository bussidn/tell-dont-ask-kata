package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.order.OrderCannotBeShippedException;
import it.gabrieletondi.telldontaskkata.domain.order.OrderCannotBeShippedTwiceException;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.doubles.TestShipmentService;
import it.gabrieletondi.telldontaskkata.useCase.shipment.OrderShipmentRequest;
import it.gabrieletondi.telldontaskkata.useCase.shipment.OrderShipmentUseCase;
import org.junit.Test;

import static it.gabrieletondi.telldontaskkata.domain.order.Order.createOrderWithId;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderShipmentUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final TestShipmentService shipmentService = new TestShipmentService();
    private final OrderShipmentUseCase useCase = new OrderShipmentUseCase(orderRepository, shipmentService);

    @Test
    public void shipApprovedOrder() {
        Order initialOrder = createOrderWithId(1)
                .status(OrderStatus.APPROVED)
                .build();
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = orderShipmentRequest();

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder().getStatus(), is(OrderStatus.SHIPPED));
        assertThat(shipmentService.getShippedOrder(), is(initialOrder.toBeShipped()));
    }

    private OrderShipmentRequest orderShipmentRequest() {
        OrderShipmentRequest request = new OrderShipmentRequest();
        request.orderId = 1;
        return request;
    }

    @Test(expected = OrderCannotBeShippedException.class)
    public void createdOrdersCannotBeShipped() {
        Order initialOrder = Order.createOrderWithId(1)
                .status(OrderStatus.CREATED).build();
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = orderShipmentRequest();

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
        assertThat(shipmentService.getShippedOrder(), is(nullValue()));
    }

    @Test(expected = OrderCannotBeShippedException.class)
    public void rejectedOrdersCannotBeShipped() {
        Order initialOrder = Order.createOrderWithId(1)
                .status(OrderStatus.REJECTED).build();
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = orderShipmentRequest();

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
        assertThat(shipmentService.getShippedOrder(), is(nullValue()));
    }

    @Test(expected = OrderCannotBeShippedTwiceException.class)
    public void shippedOrdersCannotBeShippedAgain() {
        Order initialOrder = Order.createOrderWithId(1)
                .status(OrderStatus.SHIPPED).build();
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = orderShipmentRequest();

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder(), is(nullValue()));
        assertThat(shipmentService.getShippedOrder(), is(nullValue()));
    }
}
