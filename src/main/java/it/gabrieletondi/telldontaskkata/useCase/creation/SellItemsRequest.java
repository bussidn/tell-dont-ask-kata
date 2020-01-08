package it.gabrieletondi.telldontaskkata.useCase.creation;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static it.gabrieletondi.telldontaskkata.domain.order.Order.initializeOrderWith;

public class SellItemsRequest {

    private final int id;
    private List<SellItemRequest> requests = new ArrayList<>();

    public SellItemsRequest(int id) {
        this.id = id;
    }

    OrderCreationCommand toSellItemsCommand(OrderRepository orderRepository, ProductCatalog productCatalog) {
        return new OrderCreationCommand(id, requests, orderRepository, productCatalog);
    }

    public void add(SellItemRequest sellItemRequest) {
        requests.add(sellItemRequest);
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
            orderRepository.save(toOrder());
        }

        private Order toOrder() {
            return requests.stream()
                            .map(toCommand())
                            .map(toOrderItem())
                            .reduce(initializeOrderWith(EURO, id), Order::add, (o1, o2) -> null);
        }

        private Function<SellItemRequest.SellItemCommand, OrderItem> toOrderItem() {
            return SellItemRequest.SellItemCommand::toOrderItem;
        }

        private Function<SellItemRequest, SellItemRequest.SellItemCommand> toCommand() {
            return sellItemRequest -> sellItemRequest.toSellItemCommand(productCatalog);
        }

    }
}
