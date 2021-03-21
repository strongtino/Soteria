package dev.strongtino.soteria.service.license.rest;

import dev.strongtino.soteria.service.license.License;
import dev.strongtino.soteria.service.license.product.Product;
import dev.strongtino.soteria.util.Base;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static dev.strongtino.soteria.service.license.rest.ValidationType.INVALID;
import static dev.strongtino.soteria.service.license.rest.ValidationType.VALID;
import static dev.strongtino.soteria.service.license.rest.ValidationType.WRONG_PRODUCT;

@RestController
public class LicenseController implements Base {

    @GetMapping("/license")
    public LicenseRequestResponse licenseRequestResponse(@RequestParam(value = "key", defaultValue = "Unknown") String key, @RequestParam(value = "product", defaultValue = "null") String productName) {
        License license = SOTERIA.getLicenseService().getLicenseByKey(key);
        Product product = Product.getByName(productName);

        ValidationType type = license == null ? INVALID : license.getProduct() == product ? VALID : WRONG_PRODUCT;
        LicenseRequestResponse response = new LicenseRequestResponse(type);

        if (type == VALID) {
            response.setUser(license.getUser());
            response.setProduct(license.getProduct().getName());
        }
        return response;
    }
}
