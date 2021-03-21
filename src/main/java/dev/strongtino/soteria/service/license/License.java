package dev.strongtino.soteria.service.license;

import com.google.gson.annotations.SerializedName;
import dev.strongtino.soteria.service.license.product.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class License {

    @SerializedName("_id")
    private final String key;

    private final String user;
    private final Product product;
}
