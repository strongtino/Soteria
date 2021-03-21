package dev.strongtino.soteria.service.database;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.strongtino.soteria.util.Base;
import org.bson.Document;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService implements Base {

    private final MongoDatabase database;

    public DatabaseService() {
        MongoClient client = new MongoClient(new ServerAddress(MONGO_ADDRESS, MONGO_PORT));
        database = client.getDatabase(MONGO_DATABASE);
    }

    public void insertDocument(String collection, Document document) {
        database.getCollection(collection).insertOne(document);
    }

    public void updateDocument(String collection, String key, Object value, Document document) {
        database.getCollection(collection).replaceOne(Filters.eq(key, value), document, new UpdateOptions().upsert(true));
    }

    public void deleteDocument(String collection, String key, String value) {
        database.getCollection(collection).deleteOne(Filters.eq(key, value));
    }

    @Nullable
    public Document getDocument(String collection, String key, String value) {
        return database.getCollection(collection).find(Filters.eq(key, value)).first();
    }

    public List<Document> getDocuments(String collection, String key, String value) {
        return database.getCollection(collection).find(Filters.eq(key, value)).into(new ArrayList<>());
    }

    public List<Document> getDocuments(String collection) {
        return database.getCollection(collection).find().into(new ArrayList<>());
    }

    public boolean exists(String collection, String key, Object value) {
        return database.getCollection(collection).find(Filters.eq(key, value)).first() != null;
    }
}