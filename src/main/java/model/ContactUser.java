package model;

import java.util.ArrayList;

public class ContactUser {
    private String name;
    private ArrayList<String> emails;

    public ContactUser(){
        this.emails = new ArrayList<>();
    }
    public ContactUser(String name, ArrayList<String> emails){
        this.name = name;
        this.emails = emails;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<String> getEmails() {
        return this.emails;
    }
    public void addEmail(String email) {
        this.emails.add(email);
    }

}
