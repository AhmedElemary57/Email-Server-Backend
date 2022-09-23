package DataBaseServices;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
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
            Email email = EmailsServices.fromDocumentToEmail(document);
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
