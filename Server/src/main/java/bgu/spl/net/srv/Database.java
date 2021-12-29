package bgu.spl.net.srv;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Database {

    private ConcurrentLinkedDeque<String> usernames;
    private ConcurrentHashMap<User, ConcurrentLinkedDeque<String>> userMessageQueues;
    private ConcurrentHashMap<User, ConcurrentLinkedDeque<User>> following; // a list of people followed by each user


    private static class SingletonHolder {
        private static Database instance = new Database();
    }

    public Database() {
        userMessageQueues = new ConcurrentHashMap<User, ConcurrentLinkedDeque<String>>();
    }

    public Database getInstance() {
        return Database.SingletonHolder.instance;
    }

    public ConcurrentLinkedDeque<String> getUsernames() {
        return usernames;
    }

    public ConcurrentHashMap<User, ConcurrentLinkedDeque<String>> getUserMessageQueues() {
        return userMessageQueues;
    }

    public void addUser(User user) {
        usernames.add(user.getUsername());
        userMessageQueues.put(user, new ConcurrentLinkedDeque<String>());
        following.put(user, new ConcurrentLinkedDeque<User>());
    }
}
