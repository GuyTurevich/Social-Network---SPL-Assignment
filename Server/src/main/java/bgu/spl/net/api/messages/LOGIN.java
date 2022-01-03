package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.Message;

public class LOGIN implements Message<String> {

    private String details;
    private int connectionId;

    public LOGIN(String _details, int _connectionId) {
        details = _details;
        connectionId = _connectionId;
    }

    @Override
    public void process() {
        int spaceIndex = details.indexOf(" ");
        String username = details.substring(0,spaceIndex);
        String password = details.substring(spaceIndex+1);
        if(!database.isRegistered(username)){
            // send ERROR
        }
        else if(database.isLoggedIn(username)){
            // send ERROR
        }
        else if(!database.authenticate(username,password)){
            // send ERROR
        }
        else{
            database.linkIdToUser(username, connectionId);
            // send ACK
        }

    }
}
