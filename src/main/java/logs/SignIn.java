package logs;

import DataBaseServices.UsersServices;
import model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class SignIn {

    public ResponseEntity<User> signIn(User user){

        User currentUser = UsersServices.getUserFromDB( user.getEmail());
        assert currentUser != null;
        System.out.println(currentUser.getEmail());
        if(currentUser.getPassword().equals(user.getPassword())){
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