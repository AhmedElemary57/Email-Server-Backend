package DataBaseServices;

import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.TextSearchOptions;
import model.Email;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class SearchEmails {

    public static Email[] searchEmailsInDB(String userID, String searchKey, String searchPosition){
        MongoDatabase database = DataBase.connectToDB(userID);
        MongoCollection<Document> collection = database.getCollection(searchPosition);
        collection.dropIndexes();
        collection.createIndex(Indexes.text("$**"));
        Bson filter = Filters.text(searchKey);
        System.out.println("Text search matches "+ collection.countDocuments(filter) + " documents");
        ArrayList<Email> emails = new ArrayList<>();
        for (Document document : collection.find(filter)) {
            Email email = new Email();
            email.set_id(document.get("_id").toString());
            email.setSender((String) document.get("sender"));
            email.setReceiver((String) document.get("receiver"));
            email.setSubject((String) document.get("subject"));
            email.setBody((String) document.get("content"));
            email.setDate((String) document.get("date"));
            email.setPriority((String) document.get("priority"));
            email.setSeen((boolean) document.get("seen"));
            emails.add(email);
        }
        for (Email email : emails) {
            System.out.println(email);
        }
        Email[] emailsArray = new Email[emails.size()];
        emailsArray = emails.toArray(emailsArray);
        return emailsArray;
    }

}
