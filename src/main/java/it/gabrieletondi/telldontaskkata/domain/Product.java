package it.gabrieletondi.telldontaskkata.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static java.math.RoundingMode.HALF_UP;

@Builder
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Product {

    @NonNull String name;
    @NonNull BigDecimal price;
    @NonNull Category category;

    public static Predicate<Product> byName(String name) {
        return p -> p.name.equals(name);
    }

    BigDecimal unitaryTax() {
        return category.taxRate().multiply(this.price).setScale(2, HALF_UP);
    }

    BigDecimal unitaryTaxedAmount() {
        return price.add(unitaryTax()).setScale(2, HALF_UP);
    }

}
