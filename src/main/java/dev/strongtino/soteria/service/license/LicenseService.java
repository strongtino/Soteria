package dev.strongtino.soteria.service.license;

import dev.strongtino.soteria.service.license.product.Product;
import dev.strongtino.soteria.util.Base;
import org.apache.commons.lang3.RandomStringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by StrongTino on 29.12.2020.
 */

public class LicenseService implements Base {

    private final static Map<String, License> licenses = new HashMap<>();

    public License createLicense(String user, Product product){
        String key = RandomStringUtils.randomAlphanumeric(32);

        if (SOTERIA.getDatabaseService().rowExistInTable(TABLE_LICENSES, "key", key)) {
            return createLicense(user, product);
        }
        License license = new License(key, user, product);

        SOTERIA.getDatabaseService().insertLicense(license);
        addLicenseToMap(license);

        return license;
    }

    @Nullable
    public License getLicenseByKey(String key) {
        return getLicenses().stream().filter(license -> license.getKey().equals(key)).findFirst().orElse(null);
    }

    public void addLicenseToMap(License license) {
        licenses.put(license.getKey(), license);
    }

    public Collection<License> getLicenses() {
        return licenses.values();
    }
}
