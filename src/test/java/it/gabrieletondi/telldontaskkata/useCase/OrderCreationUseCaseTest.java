package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.Category;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.domain.order.Order;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.useCase.creation.OrderCreationUseCase;
import it.gabrieletondi.telldontaskkata.useCase.creation.SellItemRequest;
import it.gabrieletondi.telldontaskkata.useCase.creation.SellItemsRequest;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static it.gabrieletondi.telldontaskkata.domain.Product.createWithName;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrderCreationUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private final Category food = new Category() {{
        setTaxPercentage(new BigDecimal("10"));
    }};
    private final ProductCatalog productCatalog = new InMemoryProductCatalog(
            Arrays.asList(
                    createWithName("salad")
                            .price("3.56")
                            .category(food)
                            .build(),
                    createWithName("tomato")
                            .price("4.65")
                            .category(food)
                            .build()
            )
    );
    private final OrderCreationUseCase useCase = new OrderCreationUseCase(orderRepository, productCatalog);

    @Test
    public void sellMultipleItems() {
        int id = 1;
        SellItemRequest saladRequest = sellItemRequest("salad", 2);

        SellItemRequest tomatoRequest = sellItemRequest("tomato", 3);

        final SellItemsRequest request = new SellItemsRequest(id);
        request.add(saladRequest);
        request.add(tomatoRequest);

        useCase.run(request);

        final Order insertedOrder = orderRepository.getSavedOrder();
        assertThat(insertedOrder.getId(), is(id));
        assertThat(insertedOrder.getStatus(), is(OrderStatus.CREATED));
        assertThat(insertedOrder.getTotal(), is(new BigDecimal("23.20")));
        assertThat(insertedOrder.getTax(), is(new BigDecimal("2.13")));
        assertThat(insertedOrder.getCurrency(), is("EUR"));
        assertThat(insertedOrder.getItems(), hasSize(2));

        assertThat(insertedOrder.getItems().get(0),
                is(new OrderItem(
                        Product.createWithName("salad").price("3.56").category(food).build(), 2)));
        assertThat(insertedOrder.getItems().get(0).taxedAmount(), is(new BigDecimal("7.84")));
        assertThat(insertedOrder.getItems().get(0).tax(), is(new BigDecimal("0.72")));

        assertThat(insertedOrder.getItems().get(1),
                is(new OrderItem(
                        Product.createWithName("tomato").price("4.65").category(food).build(), 3)));
        assertThat(insertedOrder.getItems().get(1).taxedAmount(), is(new BigDecimal("15.36")));
        assertThat(insertedOrder.getItems().get(1).tax(), is(new BigDecimal("1.41")));
    }

    private SellItemRequest sellItemRequest(String productName, int quantity) {
        SellItemRequest request = new SellItemRequest();
        request.productName = productName;
        request.quantity = quantity;
        return request;
    }

    @Test(expected = UnknownProductException.class)
    public void unknownProduct() {
        SellItemsRequest request = new SellItemsRequest(1);
        SellItemRequest unknownProductRequest = sellItemRequest("unknown product", 7);
        request.add(unknownProductRequest);

        useCase.run(request);
    }
}
