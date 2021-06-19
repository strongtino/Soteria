package dev.strongtino.soteria.license;

import dev.strongtino.soteria.Soteria;
import dev.strongtino.soteria.util.DatabaseUtil;
import dev.strongtino.soteria.util.Task;
import org.bson.Document;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class LicenseService {

    private final static Map<String, License> LICENSES = new HashMap<>();

    private final int keyLength = 32;
    private final char[] keyCharactersArray = new char[keyLength];
    private final char[] possibleKeyCharactersArray = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

    public LicenseService() {
        Task.async(() -> Soteria.INSTANCE.getDatabaseService().getDocuments(DatabaseUtil.COLLECTION_LICENSES)
                .stream()
                .map(document -> Soteria.GSON.fromJson(document.toJson(), License.class))
                .forEach(this::addLicenseToMap)
        );
        new LicenseThread().start();
    }

    public License createLicense(String user, String product) {
        License license = new License(generateLicenseKey(), user, product, Long.MAX_VALUE);

        Task.async(() -> Soteria.INSTANCE.getDatabaseService().insertDocument(DatabaseUtil.COLLECTION_LICENSES, Document.parse(Soteria.GSON.toJson(license))));
        addLicenseToMap(license);

        return license;
    }

    @Nullable
    public License getLicense(String key, String software) {
        return getLicenses().stream()
                .filter(license -> license.getKey().equals(key) && license.getSoftware().equalsIgnoreCase(software))
                .findFirst()
                .orElse(null);
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
        if (Soteria.INSTANCE.getDatabaseService().exists(DatabaseUtil.COLLECTION_LICENSES, "_id", key)) {
            return generateLicenseKey();
        }
        return new String(keyCharactersArray);
    }
}
