package it.gabrieletondi.telldontaskkata.repository;

import it.gabrieletondi.telldontaskkata.domain.Product;

import java.util.Optional;

public interface ProductCatalog {
    Optional<Product> getByName(String name);
}
