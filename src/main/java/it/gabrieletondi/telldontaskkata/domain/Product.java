package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.function.Predicate;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

public class Product {
    private final String name;
    private final BigDecimal price;
    private final Category category;
    private final BigDecimal unitaryTax;
    private final BigDecimal unitaryTaxedAmount;

    private Product(String name, BigDecimal price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.unitaryTax = calculateUnitaryTax(price, category);
        this.unitaryTaxedAmount = calculateUnitaryTaxedAmount(price, unitaryTax);
    }

    public static Product.Builder createWithName(String name) {
        return new Product.Builder(name);
    }

    public static Predicate<Product> byName(String name) {
        return p -> p.name.equals(name);
    }

    BigDecimal unitaryTax() {
        return unitaryTax;
    }

    private static BigDecimal calculateUnitaryTax(BigDecimal price, Category category) {
        return price
                .multiply(percentageRatio(category))
                .setScale(2, HALF_UP);
    }

    private static BigDecimal percentageRatio(Category category) {
        return category.getTaxPercentage()
                .divide(valueOf(100), category.getTaxPercentage().scale() + 2, RoundingMode.UNNECESSARY);
    }

    BigDecimal unitaryTaxedAmount() {
        return unitaryTaxedAmount;
    }

    private static BigDecimal calculateUnitaryTaxedAmount(BigDecimal price, BigDecimal unitaryTax) {
        return price.add(unitaryTax).setScale(2, HALF_UP);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name) &&
                price.equals(product.price) &&
                category.equals(product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, category);
    }
}
