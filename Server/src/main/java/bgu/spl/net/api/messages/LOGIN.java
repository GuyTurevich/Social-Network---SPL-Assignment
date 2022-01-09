package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;

public class LOGIN extends Message<String> {

    private String details;
    private int connectionId;
    private boolean hasLoggedIn;

    public LOGIN(String _details, int _connectionId) {
        super(Database.getInstance(), ConnectionsImpl.getInstance());
        details = _details;
        connectionId = _connectionId;
        hasLoggedIn = false;
    }


    public synchronized void process() {

        String[] arguments = details.split(" ");
        if(arguments.length != 3){
            connections.send(connectionId, "ERROR 2");
            return;
        }
        String username = arguments[0];
        String password = arguments[1];
        String captcha = arguments[2];

        if(!captcha.equals("1")){ // CAPTCHA Failed
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
            String message = database.getNextMessage(username);
            while(message != null){
                connections.send(connectionId, message);
                message = database.getNextMessage(username);
            }
            hasLoggedIn = true;
        }

    }

    public boolean hasLoggedIn() {
        return hasLoggedIn;
    }
}
