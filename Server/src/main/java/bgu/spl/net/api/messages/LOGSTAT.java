package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.Message;

import java.util.concurrent.ConcurrentLinkedDeque;

public class LOGSTAT implements Message<String> {

    private int connectionId;

    public LOGSTAT(int _connectionId) {
        connectionId = _connectionId;
    }

    @Override
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
