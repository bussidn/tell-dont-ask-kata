package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Product {
    private String name;
    private BigDecimal price;
    private Category category;

    public Product() {

    }

    public Product(String name, BigDecimal price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public static Product.Builder createWithName(String name) {
        return new Product.Builder(name);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    BigDecimal unitaryTax() {
        return getPrice()
                .divide(valueOf(100), getPrice().scale() + 2, RoundingMode.UNNECESSARY)
                .multiply(category.getTaxPercentage())
                .setScale(2, HALF_UP);
    }

    BigDecimal unitaryTaxedAmount() {
        return price.add(unitaryTax()).setScale(2, HALF_UP);
    }

    public static class Builder {
        private final String name;
        private BigDecimal price;
        private Category category;

        Builder(String name) {
            this.name = name;
        }

        public Builder price(String price) {
            this.price = new BigDecimal(price);
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Product build() {
            return new Product(name, price, category);
        }
    }
}
