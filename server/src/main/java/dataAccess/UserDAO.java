package dataAccess;
import java.util.*;
import model.UserData;
public class UserDAO {
    public Collection<UserData> userData;
    public UserDAO() {
        userData = new ArrayList<UserData>();
    }
    public void createUser(UserData user) throws DataAccessException {
        userData.add(user);
    }
    public UserData readUser(String username) throws DataAccessException{
        for (UserData user : userData) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void deleteUser() throws DataAccessException {
        userData.clear();
    }

    public boolean findUser(String username) throws DataAccessException {
        for (UserData user : userData) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

}
