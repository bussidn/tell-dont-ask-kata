package it.gabrieletondi.telldontaskkata.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String name;
    private BigDecimal price;
    private Category category;

    public String getName(){
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    BigDecimal unitaryTax() {
        return category.taxRate().multiply(this.price).setScale(2, HALF_UP);
    }

    BigDecimal getUnitaryTaxedAmount() {
        return price.add(unitaryTax()).setScale(2, HALF_UP);
    }
}
