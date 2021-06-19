package dev.strongtino.soteria.software;

import dev.strongtino.soteria.Soteria;
import dev.strongtino.soteria.util.DatabaseUtil;
import dev.strongtino.soteria.util.Task;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class SoftwareService {

    private static final Map<String, Software> SOFTWARE = new HashMap<>();

    public SoftwareService() {
        Task.async(() -> Soteria.INSTANCE.getDatabaseService().getDocuments(DatabaseUtil.COLLECTION_SOFTWARE)
                .stream()
                .map(document -> Soteria.GSON.fromJson(document.toJson(), Software.class))
                .forEach(this::addSoftwareToMap)
        );
    }

    public boolean createSoftware(String name) {
        if (SOFTWARE.containsKey(name.toLowerCase())) {
            return false;
        }
        Software software = new Software(name);

        Task.async(() -> Soteria.INSTANCE.getDatabaseService().insertDocument(DatabaseUtil.COLLECTION_SOFTWARE, Document.parse(Soteria.GSON.toJson(software))));
        addSoftwareToMap(software);

        return true;
    }

    public void addSoftwareToMap(Software software) {
        SOFTWARE.put(software.getName().toLowerCase(), software);
    }
}
