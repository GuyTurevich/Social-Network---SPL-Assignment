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

        if(details.charAt(details.lastIndexOf(" ") + 1) == '0'){ // CAPTCHA Failed
            connections.send(connectionId, "ERROR 2");
        }
        else if(!database.isRegistered(username)){ // Not registered
            connections.send(connectionId, "ERROR 2");
        }
        else if(database.isLoggedIn(username)){ // Already logged-in
            connections.send(connectionId, "ERROR 2");
        }
        else if(!database.authenticate(username,password)){ // Wrong Password
            connections.send(connectionId, "ERROR 2");
        }
        else{
            database.linkIdToUser(username, connectionId);
            connections.send(connectionId, "ACK 2");
        }

    }
}
