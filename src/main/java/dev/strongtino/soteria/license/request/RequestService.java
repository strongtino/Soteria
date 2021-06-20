package dev.strongtino.soteria.license.request;

import dev.strongtino.soteria.Soteria;
import dev.strongtino.soteria.license.LicenseController;
import dev.strongtino.soteria.software.Software;
import dev.strongtino.soteria.util.DatabaseUtil;
import dev.strongtino.soteria.util.Task;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

public class RequestService {

    public void insertRequest(String address, String key, String software, LicenseController.ValidationType type) {
        Task.async(() -> {
            Request request = new Request(getRequestsByAddress(address).size() + 1, address, key, software, System.currentTimeMillis(), type);

            Soteria.INSTANCE.getDatabaseService().insertDocument(DatabaseUtil.COLLECTION_REQUESTS, Document.parse(Soteria.GSON.toJson(request)));
        });
    }

    public List<Request> getRequestsByAddress(String address) {
        return Soteria.INSTANCE.getDatabaseService().getDocuments(DatabaseUtil.COLLECTION_REQUESTS, "address", address)
                .stream()
                .map(document -> Soteria.GSON.fromJson(document.toJson(), Request.class))
                .collect(Collectors.toList());
    }

    public List<Request> getRequestsBySoftware(Software software) {
        return Soteria.INSTANCE.getDatabaseService().getDocuments(DatabaseUtil.COLLECTION_REQUESTS, "software", software.getName())
                .stream()
                .map(document -> Soteria.GSON.fromJson(document.toJson(), Request.class))
                .collect(Collectors.toList());
    }
}
