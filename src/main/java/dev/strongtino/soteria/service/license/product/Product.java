package dev.strongtino.soteria.service.license.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum Product {

    NETFLIX("Netflix"),
    LINUX("Linux"),
    ANDROID("Android");

    private final String name;

    @Nullable
    public static Product getByName(String name) {
        return Arrays.stream(Product.values())
                .filter(product -> product.name().equalsIgnoreCase(name) || product.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
