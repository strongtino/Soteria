package dev.strongtino.soteria.service.license.rest.controller;

import dev.strongtino.soteria.service.license.License;
import dev.strongtino.soteria.service.license.product.Product;
import dev.strongtino.soteria.service.license.rest.response.LicenseRequestResponse;
import dev.strongtino.soteria.service.license.rest.validation.ValidationType;
import dev.strongtino.soteria.util.Base;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by StrongTino on 1.1.2021.
 */

@RestController
public class LicenseController implements Base {

    @GetMapping("/license")
    public LicenseRequestResponse licenseRequestResponse(@RequestParam(value = "key", defaultValue = "Unknown") String key, @RequestParam(value = "product", defaultValue = "null") String productName) {
        License license = SOTERIA.getLicenseService().getLicenseByKey(key);
        Product product = Product.getByName(productName);

        LicenseRequestResponse response = new LicenseRequestResponse(key);

        if (license == null) {
            response.setValidationType(ValidationType.INVALID);
            response.setUser("Unknown");
            response.setProduct("Unknown");
        } else {
            response.setValidationType(license.getProduct() == product ? ValidationType.VALID : ValidationType.WRONG_PRODUCT);
            response.setUser(license.getUser());
            response.setProduct(license.getProduct().getName());
        }
        return response;
    }
}
