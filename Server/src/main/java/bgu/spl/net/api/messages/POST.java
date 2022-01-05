package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.Message;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class POST implements Message<String> {

    private String details;
    private int connectionId;

    public POST(String _details, int _connectionId) {
        details = _details;
        connectionId = _connectionId;
    }

    @Override
    public void process() {
        String thisUser = database.getUsernameById(connectionId);
        String [] temp = details.split(" ");
        LinkedList<String> taggedUsers = new LinkedList<>();
        for(String word : temp){
            if(word.charAt(0) == '@')
                taggedUsers.add(word.substring(1));
        }
        ConcurrentLinkedDeque<String> usersToSend = database.getFollowersList(connectionId);
        for(String username : taggedUsers){
            if(!database.isRegistered(username)) {
                connections.send(connectionId, "ERROR 5");
                return;
            }
            else
                if(!database.isBlocked(username, thisUser) &&
                        !database.isBlocked(thisUser, username) &&
                        !database.isFollowing(username, thisUser ))
                    usersToSend.add(username);
        }
        String post = "";
        for(String username : usersToSend){
            post = "NOTIFICATION Public "+thisUser+" "+details;
            if(database.isLoggedIn(username))
                connections.send(database.getIdByUsername(username), post);
            else
                database.addMessageToQueue(username, post);
            }
        connections.send(connectionId, "ACK 5");
        database.savePost(thisUser, post);
    }
}
