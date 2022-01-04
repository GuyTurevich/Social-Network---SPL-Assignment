package bgu.spl.net.srv;

import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Database {

    private ConcurrentLinkedDeque<User> users;
    private ConcurrentHashMap<Integer, String> idToUsername;
    private ConcurrentHashMap<String, Integer> usernameToID;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> userMessageQueues;// messageQueues for logged out users
    private ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> following; // a list of people followed by each user
    private ConcurrentLinkedDeque<String> loggedInUsers;
    private ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> blockingLists;
    private String[] filteredWords = {"fuck", "shit", "sharmuta", "shtik", "shtak", "nigger"};

    private ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> PMs; // list of PM sent by each user
    private ConcurrentHashMap<String, ConcurrentLinkedDeque<String>> posts; // list of posts posted by each user

    public Database() {
        users = new ConcurrentLinkedDeque<>();
        idToUsername = new ConcurrentHashMap<>();
        usernameToID = new ConcurrentHashMap<>();
        userMessageQueues = new ConcurrentHashMap<>();
        following = new ConcurrentHashMap<>();
        loggedInUsers = new ConcurrentLinkedDeque<>();
        blockingLists = new ConcurrentHashMap<>();
        PMs = new ConcurrentHashMap<>();
        posts = new ConcurrentHashMap<>();
    }

    public void addUser(User user) {
        String username = user.getUsername();
        users.add(user);
        userMessageQueues.put(username, new ConcurrentLinkedQueue<>());
        following.put(username, new ConcurrentLinkedDeque<String>());
        blockingLists.put(username, new ConcurrentLinkedDeque<String>());
        PMs.put(username, new ConcurrentLinkedDeque<String>());
        posts.put(username, new ConcurrentLinkedDeque<String>());
    }

    public void linkIdToUser(String username, int connectionId) {
        if (usernameToID.containsKey(username)) {
            int prevId = usernameToID.get(username);
            usernameToID.replace(username, connectionId);
            idToUsername.remove(prevId);
            idToUsername.put(connectionId, username);
        } else {
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


    public boolean isRegistered(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) return true;
        }
        return false;
    }

    public ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> getUserMessageQueues() {
        return userMessageQueues;
    }


    public boolean isLoggedIn(String username) {
        return loggedInUsers.contains(username);
    }

    public boolean authenticate(String username, String password) {
        boolean isCorrect = false;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                isCorrect = user.getPassword().equals(password);
                if (isCorrect) loggedInUsers.add(username);
            }
        }
        return isCorrect; // not supposed to be reached
    }

    public String getUsernameById(int connectionId) {
        return idToUsername.get(connectionId);
    }

    public boolean isBlocked(String blockerUsername, String blockedUsername) {
        return blockingLists.get(blockerUsername).contains(blockedUsername);
    }

    public ConcurrentLinkedDeque<String> getFollowersList(int connectionId) {
        String username = idToUsername.get(connectionId);
        ConcurrentLinkedDeque<String> followersList = new ConcurrentLinkedDeque<>();
        for (User user : users) {
            if (following.get(user.getUsername()).contains(username))
                followersList.add(user.getUsername());
        }
        return followersList;
    }

    public void addMessageToQueue(String username, String message) {
        userMessageQueues.get(username).add(message);
    }

    public String filterMessage(String message) {
        for (String word : filteredWords) {
            message = message.replaceAll("(?i)" + word, "<filtered>");
        }
        return message;
    }

    public void savePM(String username, String pm) {
        PMs.get(username).add(pm);
    }

    public void savePost(String username, String pm) {
        posts.get(username).add(pm);
    }

    public ConcurrentLinkedDeque<String> getLoggedInUsers() {
        return loggedInUsers;
    }

    public String getStats(String username) {

        String userBirthday = getUserBirthday(username);
        int day = Integer.parseInt(userBirthday.substring(0, 2));
        int month = Integer.parseInt(userBirthday.substring(3, 5));
        int year = Integer.parseInt(userBirthday.substring(6));

        LocalDate birthDate = LocalDate.of(year, month, day);
        LocalDate now = LocalDate.now();
        int age = Period.between(birthDate, now).getYears();

        int numPosts = posts.get(username).size();
        int numFollowers = getFollowersList(usernameToID.get(username)).size();
        int numFollowing = following.get(username).size();

        return age + " " + numPosts + " " + numFollowers + " " + numFollowing;
    }

    public String getUserBirthday(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) return user.getBirthday();
        }
        return "problem"; //shouldn't be reached
    }

    public void block(String blockerUsername, String usernameToBlock) {

        blockingLists.get(blockerUsername).add(usernameToBlock);

        if (following.get(blockerUsername).contains(usernameToBlock))
            following.get(blockerUsername).remove(usernameToBlock);

        if (following.get(usernameToBlock).contains(blockerUsername))
            following.get(usernameToBlock).remove(blockerUsername);

    }
}
