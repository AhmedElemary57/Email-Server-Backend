package controller;

import DataBaseServices.*;
import logs.Register;
import logs.SignIn;
import model.Email;
import model.User;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
public class ServerController {

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<User> signUp(@RequestBody User user) {

        Register register = new Register();
        return register.signUp(user);
    }

    @PostMapping("/signIn")
    @ResponseBody
    public ResponseEntity<User> signIn(@RequestBody User user){
        SignIn signIn = new SignIn();
        return signIn.signIn(user);
    }


    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Boolean> upload(@RequestParam("files") List<MultipartFile> multipartFiles, @RequestParam("userID") String userID) {
        //upload data to the server
        System.out.println("uploading files" + multipartFiles.size());
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                System.out.println("uploading file................" + multipartFile.getOriginalFilename());
                UploadFileToDB.uploadFile(multipartFile.getBytes(),userID,multipartFile.getOriginalFilename());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<Boolean> send(@RequestBody Email email,@RequestParam("userID") String userID){
        System.out.println("sending email");
        System.out.println("userID: " + userID);
        return EmailsServices.sendEmail(email, userID);
    }
    @DeleteMapping("/undoRemoveFromInbox")
    public ResponseEntity<Boolean> undoRemoveFromInbox(@RequestParam String emailID, String userID){
        System.out.println("undo remove from inbox emailID = " +emailID + " userID = " + userID);
        return EmailsServices.undoRemoveFromInbox(emailID,userID);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFiles(@RequestParam String attachmentPosition,
                                              String attachmentName) {
        System.out.println("downloading file");
        String senderID = EmailsServices.getUserIDFromDB(attachmentPosition);
        return DownloadFiles.downloadFile(senderID,attachmentName);
    }


    @GetMapping("/inbox")
    public ResponseEntity<Email[]> getInbox(@RequestParam String userID){
        System.out.println("inbox of "+ userID);
        return EmailsServices.getRequestedEmails(userID, "inbox");
    }

    @GetMapping("/sent")
    public ResponseEntity<Email[]> getSent(@RequestParam String userID){
        return EmailsServices.getRequestedEmails(userID, "sent");
    }
    @GetMapping("/trash")
    public ResponseEntity<Email[]> getTrash(@RequestParam String userID){
        return EmailsServices.getRequestedEmails(userID, "trash");
    }
    @GetMapping("/draft")
    public ResponseEntity<Email[]> getDraft(@RequestParam String userID){
        return EmailsServices.getRequestedEmails(userID, "draft");
    }
    @PostMapping("/addToDrafts")
    public ResponseEntity<Boolean> addToDrafts(@RequestBody Email email, @RequestParam String userID){
        System.out.println("adding to drafts of "+ userID + " email = " + email.get_id());
        return EmailsServices.addToDrafts(userID,email);
    }

    @GetMapping("/search")
    public ResponseEntity<Email[]> search(@RequestParam String userID, String searchString, String searchPosition){
        System.out.println("searching for "+ searchString + " in " + searchPosition+ "user "+ userID);
        return new ResponseEntity<>(SearchEmails.searchEmailsInDB(userID,searchString,searchPosition), HttpStatus.OK);
    }

    @GetMapping("/sort")
    public ResponseEntity<Email[]> sort(@RequestParam String userID,
                                                      String sortBy,
                                                      String position){
        System.out.println("sorting for "+ sortBy + " in " + position+ "user "+ userID);
         return new ResponseEntity<>(SortService.sortEmailsBy(userID,sortBy,position), HttpStatus.OK);
    }
    @DeleteMapping( "/delete")
    public ResponseEntity<Boolean> deleteEmailFromDB(@RequestParam(value="userID")String userID,
                                                     @RequestParam(value="emailID") String emailID,
                                                     @RequestParam(value="position")String position) {
        System.out.println("We should delete this email from " + position);
        if(position.equals("inbox") ){
            return new ResponseEntity<>(EmailsServices.addToTrashAndRemoveFromInbox(userID,emailID), HttpStatus.OK);
        }else
            return new ResponseEntity<>(EmailsServices.removeMailFromDB(userID,emailID, position), HttpStatus.OK);
    }


    @DeleteMapping( "/markAsSeen")
    public  ResponseEntity<Boolean> markAsSeen(@RequestParam(value="userID") String userID,@RequestParam(value="emailID") String emailID ) {
        System.out.println("mark as seen "+ emailID + " user email is " + userID);
        return new ResponseEntity<>(EmailsServices.markAsSeen(userID,emailID), HttpStatus.OK);
    }

}
