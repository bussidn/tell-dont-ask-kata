package it.gabrieletondi.telldontaskkata.doubles;

import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

import java.util.List;

import static it.gabrieletondi.telldontaskkata.domain.Product.byName;

public class InMemoryProductCatalog implements ProductCatalog {
    private final List<Product> products;

    public InMemoryProductCatalog(List<Product> products) {
        this.products = products;
    }

    public Product getByName(final String name) {
        return products.stream()
                .filter(byName(name))
                .findFirst()
                .orElse(null);
    }

}
