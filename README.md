# jamon

A small vanilla Java CLI app for basic MongoDB collection and document management (no Spring framework).

Name origin: **jamon** comes from **Java + Mongo** -> **Jamon** (nwn).

It connects to a MongoDB database and lets you:
- list/create/delete collections
- switch the active collection
- list documents from the active collection
- insert a simple key/value document

Built with Java + Maven + `mongodb-driver-sync`.

## What This App Is

`jamon` is an interactive terminal menu for quick MongoDB operations.  
You start it with a MongoDB URL and a database name, then use numbered options from the menu (^_^).

## Project Structure

```text
jamon/
  pom.xml
  src/main/java/
    Main.java                    # Entry point and CLI args parsing
    cli/Menu.java                # Interactive menu and user input flow
    configuration/Database.java  # MongoDB connection and DB operations
```

## Requirements

- Java 25 (project currently compiles with source/target 25 in `pom.xml`)
- Maven 3.9+
- A running MongoDB instance (local or remote)

## Build

From the project root:

```powershell
mvn clean package
```

This creates the jar under `target/`.

Packaging note: the build uses Maven Shade Plugin, so `target\jamon-1.0-SNAPSHOT.jar`
is a fat/uber JAR that already includes runtime dependencies (MongoDB driver, etc.).
The executable `Main-Class` manifest entry is generated from `pom.xml`.

## Run

From the project root:

```powershell
java -jar target\jamon-1.0-SNAPSHOT.jar <mongoUrl|local> <databaseName>
```

Examples:

```powershell
java -jar target\jamon-1.0-SNAPSHOT.jar local myDatabase
java -jar target\jamon-1.0-SNAPSHOT.jar mongodb://localhost:27017 myDatabase
java -jar target\jamon-1.0-SNAPSHOT.jar mongodb+srv://user:pass@cluster0.example.mongodb.net myDatabase
```

Argument behavior:
- If first argument is `local`, the app uses `mongodb://localhost:27017`
- Second argument is always the database name

If arguments are missing, the app prints usage examples and exits.

## Menu Usage

After startup, the app shows:

- `0` Exit
- `1` List available collections
- `2` Create a collection
- `3` Delete collection
- `4` Switch collection
- `5` List all documents
- `6` Insert

Typical flow:
1. Start app with DB URL and DB name
2. Create a collection (`2`) or list existing ones (`1`)
3. Switch to a collection (`4`)
4. Insert documents (`6`) and list them (`5`)
5. Exit (`0`) to close DB connection

## Insert Format

Option `6` prompts for:
- `Key`
- `Value`

It inserts a document like:

```json
{ "<Key>": "<Value>" }
```

## Notes and Limitations

- The app inserts string values only in the current implementation.
- Listing/inserting requires selecting a collection first.
- Connection or timeout issues can occur if MongoDB is unreachable (>_<).

## Troubleshooting

- `MongoDB URL not provided`  
  Provide both arguments.

- `Database name not provided`  
  Provide `<databaseName>` as the second argument.

- `A collection has not been specified`  
  Use menu option `4` to switch/select a collection before listing or inserting.

## Quick Start

```powershell
mvn clean package
java -jar target\jamon-1.0-SNAPSHOT.jar local demo
```

Then in the app: `2` create collection -> `4` switch collection -> `6` insert -> `5` list.

Have fun querying (o_o)/
