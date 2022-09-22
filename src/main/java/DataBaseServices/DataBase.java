package DataBaseServices;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Collection;

public class DataBase {
    public static MongoDatabase connectToDB(String databaseName){
         ConnectionString connectionString = new ConnectionString("mongodb+srv://MailServer:Mailserver123@mailserver.5krhxva.mongodb.net/?retryWrites=true&w=majority");
         MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
         MongoClient mongoClient = MongoClients.create(settings);

        return mongoClient.getDatabase(databaseName);

    }

    public static void main(String[] args) {
        MongoDatabase database = connectToDB("ahmed@gmail.com");
        database.createCollection("Inbox");

    }

}
