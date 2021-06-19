package dev.strongtino.soteria.license.request;

import dev.strongtino.soteria.Soteria;
import dev.strongtino.soteria.license.LicenseController;
import dev.strongtino.soteria.util.DatabaseUtil;
import dev.strongtino.soteria.util.Task;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;

public class RequestService {

    public void insertRequest(String address, String key, String software, LicenseController.ValidationType type) {
        Task.async(() -> {
            Request request = new Request(getRequests(address).size() + 1, address, key, software, System.currentTimeMillis(), type);

            Soteria.INSTANCE.getDatabaseService().insertDocument(DatabaseUtil.COLLECTION_REQUESTS, Document.parse(Soteria.GSON.toJson(request)));
        });
    }

    public List<Request> getRequests(String address) {
        return Soteria.INSTANCE.getDatabaseService().getDocuments(DatabaseUtil.COLLECTION_REQUESTS, "address", address)
                .stream()
                .map(document -> Soteria.GSON.fromJson(document.toJson(), Request.class))
                .collect(Collectors.toList());
    }
}
