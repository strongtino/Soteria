package dev.strongtino.soteria.service.license;

import dev.strongtino.soteria.service.license.product.Product;
import dev.strongtino.soteria.util.Base;
import org.bson.Document;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class LicenseService implements Base {

    private final static Map<String, License> LICENSES = new HashMap<>();

    private final int keyLength = 32;
    private final char[] keyCharactersArray = new char[keyLength];
    private final char[] possibleKeyCharactersArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    public LicenseService() {
        // Caching all licenses from the database
        SOTERIA.getDatabaseService().getDocuments(COLLECTION_LICENSES)
                .stream()
                .map(document -> GSON.fromJson(document.toJson(), License.class))
                .forEach(this::addLicenseToMap);
    }

    public License createLicense(String user, Product product) {
        License license = new License(generateLicenseKey(), user, product);

        SOTERIA.getDatabaseService().insertDocument(COLLECTION_LICENSES, Document.parse(GSON.toJson(license)));
        addLicenseToMap(license);

        return license;
    }

    @Nullable
    public License getLicenseByKey(String key) {
        return getLicenses().stream().filter(license -> license.getKey().equals(key)).findFirst().orElse(null);
    }

    public void addLicenseToMap(License license) {
        LICENSES.put(license.getKey(), license);
    }

    public Collection<License> getLicenses() {
        return LICENSES.values();
    }

    public String generateLicenseKey() {
        for (int i = 0; i < keyLength; i++) {
            keyCharactersArray[i] = possibleKeyCharactersArray[ThreadLocalRandom.current().nextInt(possibleKeyCharactersArray.length)];
        }
        String key = new String(keyCharactersArray);

        // 40+ digit number of permutations, but better worry than be sorry ya know
        if (SOTERIA.getDatabaseService().exists(COLLECTION_LICENSES, "_id", key)) {
            return generateLicenseKey();
        }
        return new String(keyCharactersArray);
    }
}
