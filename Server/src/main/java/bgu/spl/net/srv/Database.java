package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Database {

    private ConcurrentLinkedDeque<User> users;
    private ConcurrentHashMap<Integer, String> idToUsername;
    private ConcurrentHashMap<String, Integer> usernameToID;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> userMessageQueues;
    private ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> following; // a list of people followed by each user
    private ConcurrentLinkedDeque<String> loggedInUsers;
    private ConcurrentHashMap<String,ConcurrentLinkedDeque<String>> blockingLists;


    public Database() {
        users = new ConcurrentLinkedDeque<>();
        idToUsername = new ConcurrentHashMap<>();
        usernameToID = new ConcurrentHashMap<>();
        userMessageQueues = new ConcurrentHashMap<>();
        following = new ConcurrentHashMap<>();
        loggedInUsers = new ConcurrentLinkedDeque<>();
        blockingLists = new ConcurrentHashMap<>();
    }

    public void linkIdToUser(String username, int connectionId) {
        if(usernameToID.containsKey(username)){
            int prevId = usernameToID.get(username);
            usernameToID.replace(username, connectionId);
            idToUsername.remove(prevId);
            idToUsername.put(connectionId, username);
        }
        else{
            usernameToID.put(username, connectionId);
            idToUsername.put(connectionId, username);
        }

    }

    public void logout(String username) {
        loggedInUsers.remove(username);
    }


    public boolean isFollowing(String username, String usernameToFollow) {
        return following.get(username).contains(usernameToFollow);
    }

    public void follow(String username, String usernameToFollow) {
        following.get(username).add(usernameToFollow);
    }

    public void unfollow(String username, String usernameToUnfollow) {
        following.get(username).remove(usernameToUnfollow);
    }

    public int getIdByUsername(String username) {
        return usernameToID.get(username);
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
    public ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> getUserMessageQueues() {
        return userMessageQueues;
    }

    public void addUser(User user) {
        users.add(user);
        userMessageQueues.put(user.getUsername(), new ConcurrentLinkedQueue<>());
        following.put(user.getUsername(), new ConcurrentLinkedDeque<String>());
        blockingLists.put(user.getUsername(), new ConcurrentLinkedDeque<String>());
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

    public String getUsernameById(int connectionId){
        return idToUsername.get(connectionId);
    }

    public boolean isBlocked(String blockerUsername, String blockedUsername){
        return blockingLists.get(blockerUsername).contains(blockedUsername);
    }

    public ConcurrentLinkedDeque<String> getFollowersList(int connectionId){
        String username = idToUsername.get(connectionId);
        ConcurrentLinkedDeque<String> followersList = new ConcurrentLinkedDeque<>();
        for (User user : users){
            if(following.get(user.getUsername()).contains(username))
                followersList.add(user.getUsername());
        }
        return followersList;
    }

    public void addMessageToQueue(String username, String message){
        userMessageQueues.get(username).add(message);
    }
}
