package org.grisu.msvc.items.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.grisu.libs.msvc.commons.entities.Product;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Product product;
    private int quantity;

    public Double total(){
        return product.getPrice() * quantity;
    }
}
