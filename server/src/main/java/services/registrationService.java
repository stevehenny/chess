package services;
import model.UserData;
import dataAccess.UserDAO;

public class registrationService {
    public registrationService() {
    }

    public void CreateUser(String username, String password) {
    }

    public void createUser(UserData user) {
        if(user == null){
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUsername() == null  || user.getUsername() == ""){
            throw new IllegalArgumentException("User must have a username");
        }
        if (user.getPassword() == null || user.getPassword() == ""){
            throw new IllegalArgumentException("User must have a password");
        }
        if (user.getEmail() == null || user.getEmail() == ""){
            throw new IllegalArgumentException("User must have an email");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
