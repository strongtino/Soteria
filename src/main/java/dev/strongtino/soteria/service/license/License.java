package dev.strongtino.soteria.service.license;

import dev.strongtino.soteria.service.license.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by StrongTino on 29.12.2020.
 */

@AllArgsConstructor
@Getter
public class License {

    private final String key, user;
    private final Product product;
}
