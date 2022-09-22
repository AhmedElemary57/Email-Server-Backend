package model;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class User {
    private String firstName;
    private String secondName;
    private String email;
    private String password;
    private String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    private int idSend;
    private int idReceive;
    private int idTrash;

    public User(String firstName, String secondName, String email, String password){
        this.firstName  = firstName;
        this.secondName = secondName;
        this.email = email;
        this.password = password;
    }

    public User (){
        this.idReceive = 0;
        this.idSend = 0;
        this.idTrash = 0;
    }

    public int getIdTrash() {
        return idTrash;
    }

    public void setIdTrash(int idTrash) {
        this.idTrash = idTrash;
    }

    public int getIdSend() {
        return idSend;
    }

    public void setIdSend(Integer idSend) {
        this.idSend = idSend;
    }

    public int getIdReceive() {
        return idReceive;
    }

    public void setIdReceive(Integer idReceive) {
        this.idReceive = idReceive;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}