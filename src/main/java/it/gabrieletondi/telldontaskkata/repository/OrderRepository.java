package it.gabrieletondi.telldontaskkata.repository;

import it.gabrieletondi.telldontaskkata.domain.order.ApprovedOrder;
import it.gabrieletondi.telldontaskkata.domain.order.Order;

import java.util.Optional;

public interface OrderRepository {
    void save(Order order);

    Order getById(int orderId);

    Optional<ApprovedOrder> findApprovedOrderById(int orderId);
}
