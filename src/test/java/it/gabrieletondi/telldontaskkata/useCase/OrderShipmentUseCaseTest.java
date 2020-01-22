package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.order.*;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.doubles.TestShipping;
import it.gabrieletondi.telldontaskkata.useCase.shipment.OrderShipmentRequest;
import it.gabrieletondi.telldontaskkata.useCase.shipment.OrderShipmentUseCase;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OrderShipmentUseCaseTest {
    public static final int ORDER_ID = 1;
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final TestShipping shipmentService = new TestShipping();
    private final OrderShipmentUseCase useCase = new OrderShipmentUseCase(orderRepository, shipmentService);

    @Test
    public void shipApprovedOrder() {
        String currency = "EUR";
        ArrayList<OrderItem> items = new ArrayList<>();
        ApprovedOrder initialOrder = new ApprovedOrder(ORDER_ID, currency, items);
        orderRepository.addOrder(initialOrder);

        OrderShipmentRequest request = orderShipmentRequest();

        useCase.run(request);

        assertThat(orderRepository.getSavedOrder().getStatus(), is(OrderStatus.SHIPPED));
        assertThat(shipmentService.getShippedOrder(), is(new SentToShippingOrder(ORDER_ID, currency, items)));
    }

    private OrderShipmentRequest orderShipmentRequest() {
        return new OrderShipmentRequest(ORDER_ID);
    }
}
