package DataBaseServices;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import model.Email;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class EmailsServices {

    public static String getUserIDFromDB(String email){
        MongoDatabase database = DataBase.connectToDB("MailServer");
        Document document = database.getCollection("Users").find(new Document("email", email)).first();
        System.out.println(document);
        if(document != null){
            return document.get("_id").toString();
        }
        return null;
    }
    public static ResponseEntity<Boolean> sendEmail(Email email, String senderID) {
        System.out.println(email.getSender());
        System.out.println(email.getReceiver());
        String receiverID = getUserIDFromDB(email.getReceiver());
        System.out.println("The sender Id is  "+senderID);
        System.out.println("The receiver Id is"+receiverID);
        if(senderID == null || receiverID == null){
            System.out.println("Error in sending email to " + email.getReceiver());
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
        }
        MongoDatabase senderDatabase = DataBase.connectToDB(senderID);
        MongoDatabase receiverDatabase = DataBase.connectToDB(receiverID);
        System.out.println("Email sent to " + email.getReceiver());
        Document document = new Document();
        document.append("sender", email.getSender());
        document.append("receiver", email.getReceiver());
        document.append("subject", email.getSubject());
        document.append("content", email.getBody());
        document.append("date", email.getDate());
        document.append("priority", email.getPriority());
        document.append("seen", email.isSeen());
        document.append("attachments", email.getAttachments());
        System.out.println("Email sent to " + email.getReceiver());
        senderDatabase.getCollection("Sent").insertOne(document);
        receiverDatabase.getCollection("Inbox").insertOne(document);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    public static Email[] getRequestedEmails(String userID, String CollectionName){
        MongoDatabase database = DataBase.connectToDB(userID);
        ArrayList<Email> emails = new ArrayList<>();
        for (Document document : database.getCollection(CollectionName).find().sort(new Document("date", -1))) {
            System.out.println(document);
            Email email = new Email();
            email.set_id(document.get("_id").toString());
            email.setSender((String) document.get("sender"));
            email.setReceiver((String) document.get("receiver"));
            email.setSubject((String) document.get("subject"));
            email.setBody((String) document.get("content"));
            email.setDate((String) document.get("date"));
            email.setPriority((String) document.get("priority"));
            email.setSeen((boolean) document.get("seen"));
            email.setAttachments((List<String>) document.get("attachments"));
            emails.add(email);
        }
        Email[] emailsArray = new Email[emails.size()];
        emailsArray = emails.toArray(emailsArray);
        for (Email email : emailsArray) {
            System.out.println(email);
        }
        return emailsArray;
    }
    public static Boolean removeMailFromInbox(Email email){
        String userEmail = email.getReceiver();
        String emailId = email.get_id();
        System.out.println(emailId + " " + userEmail);
        String userID = getUserIDFromDB(userEmail);
        MongoDatabase database = DataBase.connectToDB(userID);
        MongoCollection<Document> collection = database.getCollection("Inbox");
        System.out.println("To be deleted " + collection.find(new Document("_id", new ObjectId(emailId) )).first());
        Bson query = eq("_id", new ObjectId(emailId));
        try {
            database.getCollection("Trash").insertOne(Objects.requireNonNull(collection.find(query).first()));
            DeleteResult result = collection.deleteOne(query);
            System.out.println("Deleted document count: " + result.getDeletedCount());
            return true;
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
            return false;
        }

    }
    public static Boolean removeMailFromDB(String userID, String emailId, String collectionName){
        MongoDatabase database = DataBase.connectToDB(userID);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        System.out.println("To be deleted " + collection.find(new Document("_id", new ObjectId(emailId) )).first());
        Bson query = eq("_id", new ObjectId(emailId));
        DeleteResult result = collection.deleteOne(query);
        System.out.println("Deleted document count: " + result.getDeletedCount());
        return true;
    }

    public static Boolean markAsSeen(String userID, String emailID){
        System.out.println("The user id is " + userID);
        MongoDatabase database = DataBase.connectToDB(userID);
        MongoCollection<Document> collection = database.getCollection("Inbox");
        Bson query = eq("_id", new ObjectId(emailID));
        Document document = collection.find(query).first();
        assert document != null;
        document.replace("seen", true);
        collection.replaceOne(query, document);
        return true;
    }

}
