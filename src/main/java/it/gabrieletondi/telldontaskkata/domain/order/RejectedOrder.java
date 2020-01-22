package it.gabrieletondi.telldontaskkata.domain.order;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;

import java.util.List;
import java.util.function.Function;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.REJECTED;

public class RejectedOrder extends Order {

    RejectedOrder(int id, String currency, List<OrderItem> items) {
        super(id, REJECTED, currency, items);
    }

    public static Function<Integer, Function<String, Function<List<OrderItem>, RejectedOrder>>> constructorAsFunction() {
        return id -> currency -> items -> new RejectedOrder(id, currency, items);
    }
}
