package controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import model.ContactUser;
import model.Email;
import model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SingleTonServer {
    private static SingleTonServer server;
    private User user;
    public ArrayList<Email> sent;
    public ArrayList<Email> inbox;
    public ArrayList<Email> trash;
    public ArrayList<Email> draft;
    public ArrayList<ContactUser> contacts;
    public String attachmentId;
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    private SingleTonServer(){
        sent = new ArrayList<>();
        inbox = new ArrayList<>();
        trash = new ArrayList<>();
        draft = new ArrayList<>();
        contacts = new ArrayList<>();
    }

    public static SingleTonServer getInstance(){
        if (server == null){
            server = new SingleTonServer();
        }
        return server;
    }

    public void loadUser(User user){
        setUser(user);
        try{
            File data = new File("data\\"+user.getEmail());
            String[] folders = data.list();
            for (String folder: folders){
                if (!folder.equals("info.json") && !folder.equals("attachments")&&!folder.equals("inbox")) {
                    String path = "data\\" + user.getEmail() + "\\" + folder;
                    File f = new File(path);
                    String[] files = f.list();
                    for (String file : files) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        File jsonFile = new File(path + "\\" + file);
                        String[] jsonFiles = jsonFile.list();
                        if (!folder.equals("contacts")){
                            Email mail = objectMapper.readerFor(Email.class)
                                    .readValue(new File(jsonFile.getPath() + "\\" + jsonFiles[0]));
                            arrayFactory(folder, mail);
                        }
                        else {
                            ContactUser contactUser =  objectMapper.readerFor(ContactUser.class)
                                    .readValue(new File(jsonFile.getPath()));
                            server.contacts.add(contactUser);
                        }
                    }
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        loadInbox();
    }

    private void arrayFactory(String folder, Email email){
        switch (folder){
            case "sent":
                sent.add(email);
                break;
            case "trash":
                trash.add(email);
                break;
            case "draft":
                draft.add(email);
                break;
        }
    }

    public void resetServer(){
        user = new User();
        sent = new ArrayList<>();
        inbox = new ArrayList<>();
        trash = new ArrayList<>();
        draft = new ArrayList<>();
        contacts=new ArrayList<>();
    }

    public void loadInbox() {
        try {
            String path = "data\\" + server.getUser().getEmail() + "\\inbox";
            File f = new File(path);
            String[] files = f.list();
            if (files==null)return;
            server.inbox.clear();
            for (String file : files) {
                ObjectMapper objectMapper = new ObjectMapper();
                File jsonFile = new File(path + "\\" + file);
                String[] jsonFiles = jsonFile.list();
                Email mail = objectMapper.readerFor(Email.class)
                        .readValue(new File(jsonFile.getPath() + "\\" + jsonFiles[0]));
                server.inbox.add(mail);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
