package cli;

import com.mongodb.MongoTimeoutException;
import configuration.Database;
import org.bson.Document;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private boolean running;
    private final Scanner scanner;
    private final Database database;

    public Menu(Database database) {
        running = true;
        scanner = new Scanner(System.in);
        this.database = database;
    }

    public void handleInput() {
        display();
        System.out.print("Type an option: ");
        int option = scanner.nextInt();

        switch (option) {
            case 0:
                exit();
                break;

            case 1:
                listAvailableCollections();
                break;

            case 2:
                createCollection();
                break;

            case 3:
                deleteCollection();
                break;

            case 4:
                switchCollection();
                break;

            case 5:
                listAllDocuments();
                break;

            case 6:
                insert();
                break;

            default: break;
        }
    }

    public void display() {
        System.out.println("\n\n");
        System.out.println("---------- Database: " + database.getName() + " ---------- ");
        System.out.println("----------  Using collection: " + database.getCurrentCollectionName() + " ---------- ");
        System.out.println("0. Exit");
        System.out.println("1. List available collections");
        System.out.println("2. Create a collection");
        System.out.println("3. Delete collection");
        System.out.println("4. Switch collection");
        System.out.println("5. List all documents");
        System.out.println("6. Insert");
    }

    public boolean isStillRunning() {
        return running;
    }

    private void stopRunning() {
        running = false;
    }

    private void exit() {
        database.closeConnection();
        System.out.println("Database connection closed");
        stopRunning();
    }

    private void deleteCollection() {
        scanner.nextLine();
        System.out.print("Collection name: ");
        String name = scanner.nextLine();
        database.deleteCollection(name);
        System.out.println("Collection deleted successfully");
    }

    private void createCollection() {
        scanner.nextLine();
        System.out.print("Collection name: ");
        String name = scanner.nextLine();
        database.createCollection(name);
        System.out.println("Collection created successfully");
    }

    private void listAvailableCollections() {
        List<String> availableCollections = database.getAvailableCollections();
        System.out.println("Available collections:");
        for (String collection : availableCollections)
            System.out.println("- " + collection);
    }

    private void switchCollection() {
        scanner.nextLine();

        listAvailableCollections();

        System.out.print("Collection name: ");
        String name = scanner.nextLine();
        database.switchToThisCollection(name);

        System.out.println("Switched to collection '"+name+"' successfully");
    }

    private void listAllDocuments() {
        try {
            List<Document> documents = database.getAllDocumentsForSelectedCollection();
            System.out.println("Retrieved " + documents.size() + " documents");

            for (int i = 0 ; i < documents.size() ; i++) {
                Document document = documents.get(i);

                System.out.println("---------- " + (i + 1) + " ----------");

                System.out.println(document.toJson());

                System.out.println("--------------------");
            }
        } catch (NullPointerException e) {
            System.out.println("Failed to retrieve documents. A collection has not been specified");
        }
    }

    private void insert() {
        try {
            scanner.nextLine();
            System.out.print("Key: ");
            String key = scanner.nextLine();
            System.out.print("Value: ");
            String value = scanner.nextLine();
            Document document = new Document(key, value);
            database.insert(document);
            System.out.println("Inserted successfully");
        } catch (NullPointerException e) {
            System.out.println("Insert failed. A collection has not been specified");
        } catch (MongoTimeoutException e) {
            System.out.println("Insert failed. MongoDB timed out while waiting for a response");
        }
    }
}
