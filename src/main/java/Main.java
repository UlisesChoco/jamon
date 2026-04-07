import cli.Menu;
import configuration.Database;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("MongoDB URL not provided");
            System.out.println("Example: java -jar jamon.jar mongodb://localhost:27017 myDatabaseName");
            return;
        }

        if (args.length == 1) {
            System.out.println("Database name not provided");
            System.out.println("Example: java -jar jamon.jar mongodb://localhost:27017 myDatabaseName");
            return;
        }

        String dbUrl = (args[0].equalsIgnoreCase("local")) ? "mongodb://localhost:27017" : args[0];
        String dbName = args[1];

        Database database = new Database(dbUrl, dbName);
        Menu menu = new Menu(database);

        while (menu.isStillRunning())
            menu.handleInput();
    }
}
