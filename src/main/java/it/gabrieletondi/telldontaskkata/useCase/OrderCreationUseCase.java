package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

class OrderCreationUseCase {
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    void run(SellItemsRequest request) {
        Order order = new Order();

        for (SellItemRequest itemRequest : request.getRequests()) {
            Product product = productCatalog.getByName(itemRequest.getProductName());

            if (product == null) {
                throw new UnknownProductException();
            }
            else {
                final OrderItem orderItem = createOrderItem(itemRequest, product);

                order = order.addItem(orderItem);
            }
        }

        orderRepository.save(order);
    }

    private OrderItem createOrderItem(SellItemRequest itemRequest, Product product) {
        return OrderItem.builder()
                .product(product)
                .quantity(itemRequest.getQuantity())
                .build();
    }
}
