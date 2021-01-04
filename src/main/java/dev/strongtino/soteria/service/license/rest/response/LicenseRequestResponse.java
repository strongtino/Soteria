package dev.strongtino.soteria.service.license.rest.response;

import dev.strongtino.soteria.service.license.rest.validation.ValidationType;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by StrongTino on 1.1.2021.
 */

@Getter
@Setter
public class LicenseRequestResponse {

    private ValidationType validationType;
    private final String key;
    private String user, product;

    public LicenseRequestResponse(String key) {
        this.key = key;
    }
}
