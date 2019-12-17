package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.util.function.Function;
import java.util.stream.Stream;

class OrderCreationUseCase {
    private final OrderRepository orderRepository;
    private final ProductCatalog productCatalog;

    OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    void run(SellItemsRequest request) {
        sellItemRequests(request)
                .map(toOrderItem(productCatalog))
                .map(toOrder())
                .reduce(Order::combine)
                .ifPresent(orderRepository::save);
    }

    private Function<OrderItem, Order> toOrder() {
        return orderItem -> Order.builder().item(orderItem).build();
    }

    private Stream<SellItemRequest> sellItemRequests(SellItemsRequest request) {
        return request.getRequests()
                .stream();
    }

    private static Function<SellItemRequest, OrderItem> toOrderItem(ProductCatalog productCatalog) {
        return sellItemRequest -> {
            Product product = productCatalog.getByName(sellItemRequest.getProductName())
                .orElseThrow(UnknownProductException::new);
        return OrderItem.builder()
                .product(product)
                .quantity(sellItemRequest.getQuantity())
                .build();
        };
    }

}
