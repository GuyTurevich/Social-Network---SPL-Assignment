package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;

public class LOGOUT extends Message<String> {

    private int connectionId;
    private  BidiMessagingProtocol<String> protocol;
    private boolean hasLoggedOut;

    public LOGOUT(int _connectionId, BidiMessagingProtocol<String> _protocol) {
        super(Database.getInstance(), ConnectionsImpl.getInstance());
        connectionId = _connectionId;
        protocol = _protocol;
    }


    public void process() {

        String username = database.getUsernameById(connectionId);

        if(!database.isLoggedIn(username)){
            connections.send(connectionId, "ERROR 3");
            hasLoggedOut = false;
        }
        else{
            database.logout(username);
            connections.send(connectionId, "ACK 3");
            connections.disconnect(connectionId);
            hasLoggedOut = true;
        }

    }

    public boolean hasLoggedOut() {
        return hasLoggedOut;
    }
}
