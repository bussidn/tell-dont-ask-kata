package it.gabrieletondi.telldontaskkata.useCase.creation;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.util.List;
import java.util.function.Function;

public class SellItemsRequest {

    private final int id;
    private List<SellItemRequest> requests;

    public SellItemsRequest(int id) {
        this.id = id;
    }

    public void setRequests(List<SellItemRequest> requests) {
        this.requests = requests;
    }

    public List<SellItemRequest> getRequests() {
        return requests;
    }

    OrderCreationCommand toSellItemsCommand(OrderRepository orderRepository, ProductCatalog productCatalog) {
        return new OrderCreationCommand(id, requests, orderRepository, productCatalog);
    }

    class OrderCreationCommand {

        private static final String EURO = "EUR";

        private final int id;
        private final List<SellItemRequest> requests;
        private final OrderRepository orderRepository;
        private final ProductCatalog productCatalog;

        OrderCreationCommand(int id, List<SellItemRequest> requests, OrderRepository orderRepository, ProductCatalog productCatalog) {
            this.id = id;
            this.requests = requests;
            this.orderRepository = orderRepository;
            this.productCatalog = productCatalog;
        }

        public void run() {
            Order order = Order.initializeOrderWith(EURO, id);

            requests.stream()
                    .map(toCommand())
                    .map(toOrderItem())
                    .forEach(order::add);

            orderRepository.save(order);
        }

        private Function<SellItemRequest.SellItemCommand, OrderItem> toOrderItem() {
            return SellItemRequest.SellItemCommand::toOrderItem;
        }

        private Function<SellItemRequest, SellItemRequest.SellItemCommand> toCommand() {
            return sellItemRequest -> sellItemRequest.toSellItemCommand(productCatalog);
        }

    }
}
