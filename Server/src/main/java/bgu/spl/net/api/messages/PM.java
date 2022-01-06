package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PM extends Message<String> {

    private String details;
    private int connectionId;

    public PM(String _details, int _connectionId) {
        super(Database.getInstance(), ConnectionsImpl.getInstance());
        details = _details;
        connectionId = _connectionId;
    }


    public void process() {
        String username = database.getUsernameById(connectionId);
        String recipient = details.substring(0,details.indexOf(" "));
        String message = details.substring(details.indexOf(" ") + 1);

        if(!database.isRegistered(recipient) ||
                !database.isFollowing(username, recipient)) // also true if one of the users blocked the other
            connections.send(connectionId, "ERROR 6");
        else{
            String filteredMessage = "NOTIFICATION PM "+username+" "+database.filterMessage(message);
            filteredMessage = filteredMessage + new SimpleDateFormat(" dd-MM-yyyy HH:mm").format(new Date());
            if(database.isLoggedIn(recipient))
                connections.send(database.getIdByUsername(username), filteredMessage);
            else
                database.addMessageToQueue(username, filteredMessage);
            connections.send(connectionId, "ACK 6");
            database.savePM(username, filteredMessage);
        }

    }
}
