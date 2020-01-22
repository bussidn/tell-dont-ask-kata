package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;

import java.util.List;
import java.util.function.Function;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.SHIPPED;

public class ShippedOrder extends Order {
    ShippedOrder(int id, String currency, List<OrderItem> items) {
        super(id, SHIPPED, currency, items);
    }

    public static Function<Integer, Function<String, Function<List<OrderItem>, ShippedOrder>>> constructorAsFunction() {
        return id -> currency -> items -> new ShippedOrder(id, currency, items);
    }
}
