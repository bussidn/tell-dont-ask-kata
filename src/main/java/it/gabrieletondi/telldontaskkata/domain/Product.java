package it.gabrieletondi.telldontaskkata.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
        return divideByHundred(this.price).multiply(category.getTaxPercentage()).setScale(2, HALF_UP);
    }

    private BigDecimal divideByHundred(BigDecimal price) {
        return price.divide(valueOf(100), price.scale() + 2, RoundingMode.UNNECESSARY);
    }
}
