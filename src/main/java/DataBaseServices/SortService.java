package DataBaseServices;

import com.mongodb.client.MongoDatabase;
import model.Email;
import org.bson.Document;

import java.util.ArrayList;

public class SortService {

    public static Email[] sortEmailsBy(String userID, String sortBy, String position){
        MongoDatabase database = DataBase.connectToDB(userID);
        ArrayList<Email> emails = new ArrayList<>();
        int sortType=1;
        if(sortBy.equals("date")){
            sortType= -1;
        }
        for (Document document : database.getCollection(position).find().sort(new Document(sortBy, sortType))) {
            System.out.println(document);
            Email email = EmailsServices.fromDocumentToEmail(document);
            emails.add(email);
        }
        Email[] emailsArray = new Email[emails.size()];
        emailsArray = emails.toArray(emailsArray);
        for (Email email : emailsArray) {
            System.out.println(email);
        }
        return emailsArray;
    }
}
