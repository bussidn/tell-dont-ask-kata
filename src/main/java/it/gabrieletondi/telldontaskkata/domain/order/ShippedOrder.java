package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;

import java.util.List;
import java.util.function.Function;

public class ShippedOrder extends Order {
    ShippedOrder(int id, OrderStatus status, String currency, List<OrderItem> items) {
        super(id, status, currency, items);
    }

    public static Function<Integer,
            Function<OrderStatus,
                    Function<String,
                            Function<List<OrderItem>, ShippedOrder>>>> constructorAsFunction() {
        return id -> status -> currency -> items -> new ShippedOrder(id, status, currency, items);
    }
}
