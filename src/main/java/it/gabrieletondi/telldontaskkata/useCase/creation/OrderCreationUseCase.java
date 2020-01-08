package it.gabrieletondi.telldontaskkata.useCase.creation;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.useCase.UnknownProductException;

public class OrderCreationUseCase {
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    public OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    public void run(SellItemsRequest request) {
        Order order = Order.createOrderWithId(request.orderId())
                .status(OrderStatus.CREATED)
                .currency("EUR")
                .build();

        for (SellItemRequest itemRequest : request.getRequests()) {
            Product product = productCatalog.findByName(itemRequest.getProductName())
                    .orElseThrow(UnknownProductException::new);

            int quantity = itemRequest.getQuantity();

            final OrderItem orderItem = orderItem(product, quantity);
            order.getItems().add(orderItem);
        }

        orderRepository.save(order);
    }

    private OrderItem orderItem(Product product, int quantity) {
        return new OrderItem(product, quantity);
    }

}
