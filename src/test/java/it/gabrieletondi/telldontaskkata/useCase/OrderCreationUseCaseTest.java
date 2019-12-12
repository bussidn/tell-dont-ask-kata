package it.gabrieletondi.telldontaskkata.useCase;

import it.gabrieletondi.telldontaskkata.domain.*;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrderCreationUseCaseTest {
    private final TestOrderRepository orderRepository = new TestOrderRepository();
    private Category food = Category.builder()
            .name("food")
            .taxPercentage(new BigDecimal("10")).build();

    private final ProductCatalog productCatalog = new InMemoryProductCatalog(
            products()
    );

    private List<Product> products() {
        return asList(
                product("salad", "3.56"),
                product("tomato", "4.65")
        );
    }

    private Product product(String name, String price) {
        return Product.builder()
                .name(name)
                .price(new BigDecimal(price))
                .category(food)
                .build();
    }

    private final OrderCreationUseCase useCase = new OrderCreationUseCase(orderRepository, productCatalog);

    @Test
    public void sellMultipleItems() {
        SellItemRequest saladRequest = new SellItemRequest();
        saladRequest.setProductName("salad");
        saladRequest.setQuantity(2);

        SellItemRequest tomatoRequest = new SellItemRequest();
        tomatoRequest.setProductName("tomato");
        tomatoRequest.setQuantity(3);

        final SellItemsRequest request = new SellItemsRequest();
        request.setRequests(new ArrayList<>());
        request.getRequests().add(saladRequest);
        request.getRequests().add(tomatoRequest);

        useCase.run(request);

        final Order insertedOrder = orderRepository.getSavedOrder();

        assertThat(insertedOrder, is(Order.builder()
                .status(OrderStatus.CREATED)
                .currency("EUR")
                .items(asList(
                        OrderItem.builder()
                                .product(Product.builder().category(food).name("salad").price(new BigDecimal("3.56")).build())
                                .quantity(2)
                                .build(),
                        OrderItem.builder()
                                .product(Product.builder().category(food).name("tomato").price(new BigDecimal("4.65")).build())
                                .quantity(3)
                                .build()
                        )
                ).build()
        ));


        assertThat(insertedOrder.getItems().get(0).getTaxedAmount(), is(new BigDecimal("7.84")));
        assertThat(insertedOrder.getItems().get(0).getTax(), is(new BigDecimal("0.72")));
        assertThat(insertedOrder.getItems().get(1).getTaxedAmount(), is(new BigDecimal("15.36")));
        assertThat(insertedOrder.getItems().get(1).getTax(), is(new BigDecimal("1.41")));
        assertThat(insertedOrder.getTotal(), is(new BigDecimal("23.20")));
        assertThat(insertedOrder.getTax(), is(new BigDecimal("2.13")));
    }

    @Test(expected = UnknownProductException.class)
    public void unknownProduct() {
        SellItemsRequest request = new SellItemsRequest();
        request.setRequests(new ArrayList<>());
        SellItemRequest unknownProductRequest = new SellItemRequest();
        unknownProductRequest.setProductName("unknown product");
        request.getRequests().add(unknownProductRequest);

        useCase.run(request);
    }
}
