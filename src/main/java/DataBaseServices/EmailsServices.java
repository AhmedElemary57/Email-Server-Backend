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
    public static void addEmailToCollection(String userID, String collectionName, Email email){
        MongoDatabase database = DataBase.connectToDB(userID);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document document = new Document();
        document.append("sender", email.getSender());
        document.append("receiver", email.getReceiver());
        document.append("subject", email.getSubject());
        document.append("content", email.getBody());
        document.append("date", email.getDate());
        document.append("priority", email.getPriority());
        document.append("seen", email.isSeen());
        document.append("attachments", email.getAttachments());
        collection.insertOne(document);
    }
    public static void removeEmailFromCollection(String userID, String collectionName, String emailID){
        MongoDatabase database = DataBase.connectToDB(userID);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        collection.deleteOne(eq("_id", new ObjectId(emailID)));
    }
    public static void updateEmailInCollection(String userID, String collectionName, Email email){
        MongoDatabase database = DataBase.connectToDB(userID);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document document = new Document();
        document.append("sender", email.getSender());
        document.append("receiver", email.getReceiver());
        document.append("subject", email.getSubject());
        document.append("content", email.getBody());
        document.append("date", email.getDate());
        document.append("priority", email.getPriority());
        document.append("seen", email.isSeen());
        document.append("attachments", email.getAttachments());
        collection.updateOne(eq("_id", new ObjectId(email.get_id())), new Document("$set", document));
    }
    public static Email getEmailFromCollection(String userID, String collectionName, String emailID){
        MongoDatabase database = DataBase.connectToDB(userID);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document document = collection.find(eq("_id", new ObjectId(emailID))).first();
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
        return email;
    }
    public static ResponseEntity<Boolean> sendEmail(Email email, String senderID) {
        System.out.println("Sender: "+email.getSender()+ " Receiver: "+email.getReceiver());
        String receiverID = getUserIDFromDB(email.getReceiver());
        System.out.println("The sender Id is  "+senderID);
        System.out.println("The receiver Id is"+receiverID);
        if(senderID == null || receiverID == null || senderID.equals(receiverID)){
            System.out.println("Error in sending email to " + email.getReceiver());
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
        }
        addEmailToCollection(senderID, "sent", email);
        addEmailToCollection(receiverID, "inbox", email);
        System.out.println("Email sent to " + email.getReceiver());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
    public static ResponseEntity<Email[]> getRequestedEmails(String userID, String CollectionName){
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
        return new ResponseEntity<>(emailsArray,HttpStatus.OK);
    }
    public static Boolean addToTrashAndRemoveFromInbox(String userID, String emailID){
        System.out.println("Remove Email with Id "+emailID + " From trash of user with Id "+ userID);
        addEmailToCollection(userID, "trash", getEmailFromCollection(userID, "inbox", emailID));
        removeEmailFromCollection(userID, "inbox", emailID);
        return true;

    }
    public static Boolean removeMailFromDB(String userID, String emailId, String collectionName){
        System.out.println("Remove Email with Id "+emailId + " From "+collectionName+" of user with Id "+ userID);
        removeEmailFromCollection(userID, collectionName, emailId);
        return true;
    }
    public static Boolean markAsSeen(String userID, String emailID){
        System.out.println("The user id is " + userID);
        MongoDatabase database = DataBase.connectToDB(userID);
        MongoCollection<Document> collection = database.getCollection("inbox");
        Bson query = eq("_id", new ObjectId(emailID));
        Document document = collection.find(query).first();
        assert document != null;
        document.replace("seen", true);
        collection.replaceOne(query, document);
        return true;
    }
    public static  ResponseEntity<Boolean> undoRemoveFromInbox(String emailID, String userID){
        MongoDatabase database = DataBase.connectToDB(userID);
        MongoCollection<Document> collection = database.getCollection("trash");
        Bson query = eq("_id", new ObjectId(emailID));
        Document document = collection.find(query).first();
        System.out.println(document);
        assert document != null;
        database.getCollection("inbox").insertOne(document);
        collection.deleteOne(query);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
    public static ResponseEntity<Boolean> addToDrafts(String userID, Email email){
        addEmailToCollection(userID, "draft", email);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
