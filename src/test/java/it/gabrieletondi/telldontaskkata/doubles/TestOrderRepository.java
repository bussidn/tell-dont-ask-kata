package it.gabrieletondi.telldontaskkata.doubles;

import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestOrderRepository implements OrderRepository {
    private Order insertedOrder;
    private Set<Order> orders = new HashSet<>();

    public Order getSavedOrder() {
        return insertedOrder;
    }

    public void save(Order order) {
        this.insertedOrder = order;
        orders.remove(order);
        orders.add(order);
    }

    @Override
    public Order getById(int orderId) {
        return orders.stream().filter(o -> o.getId() == orderId).findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}
