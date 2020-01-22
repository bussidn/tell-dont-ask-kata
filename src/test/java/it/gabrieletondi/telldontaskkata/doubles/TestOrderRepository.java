package it.gabrieletondi.telldontaskkata.doubles;

import it.gabrieletondi.telldontaskkata.domain.order.ApprovedOrder;
import it.gabrieletondi.telldontaskkata.domain.order.CreatedOrder;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TestOrderRepository implements OrderRepository {
    private Order insertedOrder;
    private final Set<Order> orders = new HashSet<>();
    private final Set<ApprovedOrder> approvedOrders = new HashSet<>();
    private final Set<CreatedOrder> createdOrders = new HashSet<>();

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

    @Override
    public Optional<ApprovedOrder> findApprovedOrderById(int orderId) {
        return approvedOrders.stream().filter(o -> o.getId() == orderId).findFirst();
    }

    @Override
    public Optional<CreatedOrder> findCreatedOrderById(int orderId) {
        return createdOrders.stream().filter(o -> o.getId() == orderId).findFirst();
    }

    public void addOrder(ApprovedOrder order) {
        this.approvedOrders.add(order);
    }

    public void addOrder(CreatedOrder order) {
        this.createdOrders.add(order);
    }
}
