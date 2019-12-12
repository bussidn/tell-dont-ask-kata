package it.gabrieletondi.telldontaskkata.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static java.math.RoundingMode.HALF_UP;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Product {

    private String name;
    private BigDecimal price;
    private Category category;

    public static Predicate<Product> byName(String name) {
        return p -> p.name.equals(name);
    }

    public String getName(){
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    BigDecimal unitaryTax() {
        return category.taxRate().multiply(this.price).setScale(2, HALF_UP);
    }

    BigDecimal unitaryTaxedAmount() {
        return price.add(unitaryTax()).setScale(2, HALF_UP);
    }

}
