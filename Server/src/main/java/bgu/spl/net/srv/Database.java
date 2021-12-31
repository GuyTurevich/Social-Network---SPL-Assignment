package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Database {

    private ConcurrentLinkedDeque<User> users;
    private ConcurrentHashMap<Integer, User> idToUser;
    private ConcurrentHashMap<User, Integer> userToID;
    private ConcurrentHashMap<User, ConcurrentLinkedDeque<String>> userMessageQueues;
    private ConcurrentHashMap<User, ConcurrentLinkedDeque<User>> following; // a list of people followed by each user
    private ConcurrentLinkedDeque<String> loggedInUsers;


    public Database() {
        users = new ConcurrentLinkedDeque<>();
        idToUser = new ConcurrentHashMap<>();
        userToID = new ConcurrentHashMap<>();
        userMessageQueues = new ConcurrentHashMap<>();
        following = new ConcurrentHashMap<>();
        loggedInUsers = new ConcurrentLinkedDeque<>();
    }

    private static class SingletonHolder {
        private static Database instance = new Database();
    }
    public static Database getInstance() {
        return Database.SingletonHolder.instance;
    }


    public boolean isRegistered(String username){
        for (User user : users) {
            if(user.getUsername().equals(username)) return true;
        }
        return false;
    }
    public ConcurrentHashMap<User, ConcurrentLinkedDeque<String>> getUserMessageQueues() {
        return userMessageQueues;
    }

    public void addUser(User user) {
        users.add(user);
        userMessageQueues.put(user, new ConcurrentLinkedDeque<String>());
        following.put(user, new ConcurrentLinkedDeque<User>());
    }

    public boolean isLoggedIn(String username) {
        return loggedInUsers.contains(username);
    }

    public boolean authenticate(String username, String password) {
        boolean isCorrect = false;
        for (User user : users){
            if(user.getUsername().equals(username)) {
                isCorrect = user.getPassword().equals(password);
                if (isCorrect) loggedInUsers.add(username);
            }
        }
        return isCorrect; // not supposed to be reached
    }
}
