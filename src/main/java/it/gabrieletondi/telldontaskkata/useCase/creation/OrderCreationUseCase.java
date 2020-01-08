package it.gabrieletondi.telldontaskkata.useCase.creation;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.useCase.UnknownProductException;

import java.math.BigDecimal;
import java.util.ArrayList;

import static java.math.RoundingMode.HALF_UP;

public class OrderCreationUseCase {
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    public OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    public void run(SellItemsRequest request) {
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setItems(new ArrayList<>());
        order.setCurrency("EUR");
        order.setTotal(new BigDecimal("0.00"));
        order.setTax(new BigDecimal("0.00"));

        for (SellItemRequest itemRequest : request.getRequests()) {
            Product product = productCatalog.findByName(itemRequest.getProductName())
                    .orElseThrow(UnknownProductException::new);

            int quantity = itemRequest.getQuantity();

            final OrderItem orderItem = orderItem(product, quantity);

            final BigDecimal taxedAmount = product.unitaryTaxedAmount().multiply(BigDecimal.valueOf(quantity)).setScale(2, HALF_UP);
            final BigDecimal taxAmount = product.unitaryTax().multiply(BigDecimal.valueOf(quantity));

            orderItem.setTax(taxAmount);
            orderItem.setTaxedAmount(taxedAmount);
            order.getItems().add(orderItem);

            order.setTotal(order.getTotal().add(taxedAmount));
            order.setTax(order.getTax().add(taxAmount));
        }

        orderRepository.save(order);
    }

    private OrderItem orderItem(Product product, int quantity) {
        final OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        return orderItem;
    }

}
