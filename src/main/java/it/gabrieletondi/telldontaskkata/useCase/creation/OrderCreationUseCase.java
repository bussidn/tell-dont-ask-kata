package it.gabrieletondi.telldontaskkata.useCase.creation;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.useCase.UnknownProductException;

public class OrderCreationUseCase {

    private static final String EURO = "EUR";
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    public OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    public void run(SellItemsRequest request) {
        Order order = Order.initializeOrderWith(EURO, request.orderId());

        for (SellItemRequest itemRequest : request.getRequests()) {
            order.add(orderItemFrom(itemRequest));
        }

        orderRepository.save(order);
    }

    private OrderItem orderItemFrom(SellItemRequest itemRequest) {
        return orderItem(productFrom(itemRequest), quantityFrom(itemRequest));
    }

    private int quantityFrom(SellItemRequest itemRequest) {
        return itemRequest.getQuantity();
    }

    private Product productFrom(SellItemRequest itemRequest) {
        return productCatalog.findByName(itemRequest.getProductName())
                .orElseThrow(UnknownProductException::new);
    }

    private OrderItem orderItem(Product product, int quantity) {
        return new OrderItem(product, quantity);
    }

}
