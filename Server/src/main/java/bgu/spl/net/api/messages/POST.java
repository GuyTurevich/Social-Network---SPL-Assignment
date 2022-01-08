package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class POST extends Message<String> {

    private String details;
    private int connectionId;

    public POST(String _details, int _connectionId) {
        super(Database.getInstance(), ConnectionsImpl.getInstance());
        details = _details;
        connectionId = _connectionId;
    }

    public void process() {
        String thisUser = database.getUsernameById(connectionId);
        String[] temp = details.split(" ");
        LinkedList<String> taggedUsers = new LinkedList<>();
        for (String word : temp) {
            if (word.charAt(0) == '@')
                taggedUsers.add(word.substring(1));
        }
        ConcurrentLinkedDeque<String> usersToSend = database.getFollowersList(database.getUsernameById(connectionId));
        for (String username : taggedUsers) {
            if (database.isRegistered(username) &&
                    !database.isBlocked(username, thisUser) &&
                    !database.isBlocked(thisUser, username) &&
                    !database.isFollowing(username, thisUser))
                usersToSend.add(username);
        }
        String post = "";
        for (String username : usersToSend) {
            post = "NOTIFICATION Public " + thisUser + " " + details;
            if (database.isLoggedIn(username))
                connections.send(database.getIdByUsername(username), post);
            else
                database.addMessageToQueue(username, post);
        }
        connections.send(connectionId, "ACK 5");
        database.savePost(thisUser, post);
    }
}
