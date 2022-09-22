package logs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LogOut {

//    public void save(){
//        try {
//            String[] files={"sent","trash","draft"};
//
//            for (String whatToSave:files) {
//
//                String dirPath =
//                        "data\\" + server.getUser().getEmail() + "\\" + whatToSave;
//
//                File dir = new File(dirPath);
//                removeDirectory(dir);
//
//                for (int i = 0; i < getListOf(whatToSave).size(); i++) {
//
//                    String pathSent =
//                            "data\\" + server.getUser().getEmail() + "\\" + whatToSave + "\\" + i;
//
//                    FolderFactory factory = new FolderFactory();
//                    factory.createFolder(pathSent);
//                    pathSent += "\\" + getListOf(whatToSave).get(i).getReceiver() + ".json";
//
//                    JsonFactory jsonFactory = new JsonFactory();
//                    jsonFactory.createJsonFile(new File(pathSent), getListOf(whatToSave).get(i));
//                }
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//        saveInbox();
//        saveContacts();
//        server.resetServer();
//    }
//
//    public void saveInbox(){
//        try {
//            String dirPath =
//                    "data\\" + server.getUser().getEmail() + "\\" + "inbox";
//
//            File dir = new File(dirPath);
//            removeDirectory(dir);
//
//            for (int i = 0; i < getListOf("inbox").size(); i++) {
//
//                String pathSent =
//                        "data\\" + server.getUser().getEmail() + "\\" + "inbox" + "\\" + i;
//
//                FolderFactory factory = new FolderFactory();
//                factory.createFolder(pathSent);
//                pathSent += "\\" + getListOf("inbox").get(i).getReceiver() + ".json";
//
//                JsonFactory jsonFactory = new JsonFactory();
//                jsonFactory.createJsonFile(new File(pathSent), getListOf("inbox").get(i));
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public void saveContacts(){
//        try {
//
//            String dirPath =
//                    "data\\" + server.getUser().getEmail() + "\\" + "contacts";
//
//            File dir = new File(dirPath);
//            removeDirectory(dir);
//
//            String pathSent =
//                    "data\\" + server.getUser().getEmail() + "\\" + "contacts" ;
//
//            FolderFactory factory = new FolderFactory();
//            factory.createFolder(pathSent);
//            for (int i = 0; i < server.contacts.size() ; i++) {
//
//                pathSent =
//                        "data\\" + server.getUser().getEmail()
//                                + "/contacts/" +server.contacts.get(i).getName() + ".json";
//
//                JsonFactory jsonFactory = new JsonFactory();
//                jsonFactory.createJsonFile(new File(pathSent), server.contacts.get(i));
//            }
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }
//
//    public void refresh(){
//        server.loadInbox();
//        saveInbox();
//        User tempUser = server.getUser();
//        save();
//        server.loadUser(tempUser);
//    }
//
//    private void removeDirectory(File dir) {
//        if (dir.isDirectory()) {
//            File[] files = dir.listFiles();
//            if (files != null && files.length > 0) {
//                for (File aFile : files) {
//                    removeDirectory(aFile);
//                }
//            }
//            dir.delete();
//        } else {
//            dir.delete();
//        }
//    }
//
//    private ArrayList<Email> getListOf(String whatToSave){
//        switch (whatToSave){
//            case "sent":
//                return server.sent;
//            case "draft":
//                return server.draft;
//            case "inbox":
//                return server.inbox;
//            default:
//                return server.trash;
//        }
//    }
}