package logs;

import DataBaseServices.UsersServices;
import controller.SingleTonServer;
import model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SignIn {
    SingleTonServer server;

    public ResponseEntity<User> signIn(User user){

        server = SingleTonServer.getInstance();
        User currentUser = UsersServices.getUserFromDB( user.getEmail());
        System.out.println(currentUser.getEmail());
        if (currentUser == null){
            System.out.println("user not found");
            return new ResponseEntity<>(null,HttpStatus.NOT_ACCEPTABLE);
        }
        else if(currentUser.getPassword().equals(user.getPassword())){
            System.out.println("Password is correct");
            System.out.println("password: " + currentUser.getPassword() + " Given " + user.getPassword());
            return new ResponseEntity<>(currentUser, HttpStatus.ACCEPTED);
        }
        else {
            System.out.println("Password is incorrect");
            System.out.println("password: " + currentUser.getPassword() + " Given " + user.getPassword());
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}