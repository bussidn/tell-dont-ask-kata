package it.gabrieletondi.telldontaskkata.useCase.creation;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.useCase.UnknownProductException;

import java.util.List;

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

    SellItemsCommand toSellItemsCommand(OrderRepository orderRepository, ProductCatalog productCatalog) {
        return new SellItemsCommand(id, requests, orderRepository, productCatalog);
    }

    class SellItemsCommand {

        private static final String EURO = "EUR";

        private final int id;
        private final List<SellItemRequest> requests;
        private final OrderRepository orderRepository;
        private final ProductCatalog productCatalog;

        SellItemsCommand(int id, List<SellItemRequest> requests, OrderRepository orderRepository, ProductCatalog productCatalog) {
            this.id = id;
            this.requests = requests;
            this.orderRepository = orderRepository;
            this.productCatalog = productCatalog;
        }

        public void run() {
            Order order = Order.initializeOrderWith(EURO, id);

            for (SellItemRequest itemRequest : requests) {
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
}
