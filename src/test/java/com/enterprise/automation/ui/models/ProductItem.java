package com.enterprise.automation.ui.models;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductItem {
    private final String name;
    private final BigDecimal price;

    public ProductItem(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ProductItem that)) {
            return false;
        }
        return Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        return "ProductItem{name='" + name + "', price=" + price + "}";
    }
}
