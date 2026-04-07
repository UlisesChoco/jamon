package configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private String url;
    private String name;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection;

    public Database(String url, String name) {
        this.url = url;
        this.name = name;
        connectToMongoDB();
    }

    private void connectToMongoDB() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(url))
                .serverApi(serverApi)
                .build();

        mongoClient = MongoClients.create(mongoClientSettings);
        mongoDatabase = mongoClient.getDatabase(name);
    }

    public List<Document> getAllDocumentsForSelectedCollection() {
        List<Document> documents = new ArrayList<>();
        FindIterable<Document> findIterable = mongoCollection.find();

        try (MongoCursor<Document> iterator = findIterable.iterator()) {
            while (iterator.hasNext())
                documents.add(iterator.next());
        }

        return documents;
    }

    public void deleteCollection(String name) {
        switchToThisCollection(name);
        mongoCollection.drop();
    }

    public void createCollection(String name) {
        mongoDatabase.createCollection(name);
    }

    public List<String> getAvailableCollections() {
        List<String> availableCollections = new ArrayList<>();
        ListCollectionNamesIterable names = mongoDatabase.listCollectionNames();

        try (MongoCursor<String> iterable = names.iterator()) {
            while (iterable.hasNext())
                availableCollections.add(iterable.next());
        }

        return availableCollections;
    }

    public void switchToThisCollection(String name) {
        mongoCollection = mongoDatabase.getCollection(name);
    }

    public void insert(Document document) {
        mongoCollection.insertOne(document);
    }

    public void closeConnection() {
        mongoClient.close();
    }

    public String getName() {
        return name;
    }

    public String getCurrentCollectionName() {
        if (mongoCollection == null)
            return "No collection selected";

        return mongoCollection.getNamespace().getCollectionName();
    }
}
