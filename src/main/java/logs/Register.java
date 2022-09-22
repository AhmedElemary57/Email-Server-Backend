package logs;

import DataBaseServices.UsersServices;

import model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Register {
    public ResponseEntity<User> signUp(User user) {
        // check if the user already exists
        if (UsersServices.getUserFromDB(user.getEmail()) != null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
        }
        else {
            UsersServices.creatNewUser(user);
            System.out.println("User added");
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }
}