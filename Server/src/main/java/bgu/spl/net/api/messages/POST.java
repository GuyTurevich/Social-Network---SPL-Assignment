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
        String [] temp = details.split(" ");
        LinkedList<String> taggedUsers = new LinkedList<>();
        for(String word : temp){
            if(word.charAt(0) == '@')
                taggedUsers.add(word.substring(1));
        }
        ConcurrentLinkedDeque<String> followers = database.getFollowersList(connectionId);
        for(String username : taggedUsers){
            if(!database.isRegistered(username)) {
                connections.send(connectionId, "ERROR 5");
                return;
            }
            else
                if(!database.isBlocked(username, database.getUsernameById(connectionId)))
                    followers.add(username);
        }
        for(String username : followers){
            if(database.isLoggedIn(username))
                connections.send(database.getIdByUsername(username),"NOTIFICATION Public " +  details);
            else
                database.addMessageToQueue(username, "NOTIFICATION Public " + details);
            }
        connections.send(connectionId, "ACK 5");
    }
}
