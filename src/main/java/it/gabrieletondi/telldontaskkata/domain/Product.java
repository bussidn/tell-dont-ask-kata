package it.gabrieletondi.telldontaskkata.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String name;
    private BigDecimal price;
    private Category category;

    public Optional<String> getName(){
        return Optional.ofNullable(name);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }
}
