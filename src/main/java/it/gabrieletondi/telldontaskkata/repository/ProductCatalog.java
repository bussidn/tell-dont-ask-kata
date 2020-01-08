package it.gabrieletondi.telldontaskkata.repository;

import it.gabrieletondi.telldontaskkata.domain.Product;

import java.util.Optional;

public interface ProductCatalog {
    Product getByName(String name);
    Optional<Product> findByName(String name);
}
