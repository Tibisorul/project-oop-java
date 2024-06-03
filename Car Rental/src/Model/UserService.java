package Model;

import java.util.*;

public class UserService {
    private Map<Integer, User> users;

    public UserService() {
        users = new HashMap<>();
    }

    public void addUser(User user) {
        users.put(user.getID(), user);
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public void removeUser(int id) {
        users.remove(id);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public void clearUsers() {
        users.clear();
    }
}
