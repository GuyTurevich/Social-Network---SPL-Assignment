package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;

public class BLOCK extends Message<String> {

    private String details;
    private int connectionId;

    public BLOCK(String _details, int _connectionId) {
        super(Database.getInstance(), ConnectionsImpl.getInstance());
        details = _details;
        connectionId = _connectionId;
    }


    public void process() {

        String blockerUsername = database.getUsernameById(connectionId);
        String usernameToBlock = details;
        if(!database.isRegistered(usernameToBlock) ||
                database.isBlocked(blockerUsername, usernameToBlock) ||
                database.isBlocked(usernameToBlock, blockerUsername))
            connections.send(connectionId, "ERROR 12");
        else{
            database.block(blockerUsername, usernameToBlock);
            connections.send(connectionId, "ACK 12");
        }
    }
}
