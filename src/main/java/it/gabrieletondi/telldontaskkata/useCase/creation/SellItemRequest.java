package it.gabrieletondi.telldontaskkata.useCase.creation;

import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;
import it.gabrieletondi.telldontaskkata.useCase.UnknownProductException;

public class SellItemRequest {
    public int quantity;
    public String productName;

    SellItemCommand toSellItemCommand(ProductCatalog productCatalog) {
        return new SellItemCommand(productName, quantity, productCatalog);
    }

    class SellItemCommand {

        private final String productName;
        private final int quantity;
        private final ProductCatalog productCatalog;

        SellItemCommand(String productName, int quantity, ProductCatalog productCatalog) {
            this.productName = productName;
            this.quantity = quantity;
            this.productCatalog = productCatalog;
        }

        OrderItem toOrderItem() {
            return new OrderItem(findProductWithName(productName), quantity);
        }

        private Product findProductWithName(String productName) {
            return productCatalog.findByName(productName)
                    .orElseThrow(UnknownProductException::new);
        }
    }

}
