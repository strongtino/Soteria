package dev.strongtino.soteria.service.license.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Created by StrongTino on 28.12.2020.
 */

@AllArgsConstructor
@Getter
public enum Product {

    SIGMA("Sigma"),
    FFA("FFA"),
    FREEZE("Freeze");

    private final String name;

    @Nullable
    public static Product getByName(String name) {
        return Arrays.stream(Product.values())
                .filter(product -> product.name().equalsIgnoreCase(name) || product.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
