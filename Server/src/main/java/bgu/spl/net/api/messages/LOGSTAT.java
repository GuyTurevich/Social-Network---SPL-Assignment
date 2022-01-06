package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;

import java.util.concurrent.ConcurrentLinkedDeque;

public class LOGSTAT extends Message<String> {

    private int connectionId;

    public LOGSTAT(int _connectionId) {
        super(Database.getInstance(), ConnectionsImpl.getInstance());
        connectionId = _connectionId;
    }


    public void process() {
        String thisUser = database.getUsernameById(connectionId);
        String output = "";
        ConcurrentLinkedDeque<String> loggedInUsers = database.getLoggedInUsers();
        for (String username : loggedInUsers) {
            if (!database.isBlocked(username, thisUser) && !database.isBlocked(thisUser, username))
                output += "ACK 7 " + database.getStats(username) + "\n";
        }
        connections.send(connectionId, output);
    }
}
