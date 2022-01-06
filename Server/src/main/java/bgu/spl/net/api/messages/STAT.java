package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;

public class STAT extends Message<String> {

    private String details;
    private int connectionId;

    public STAT(String _details, int _connectionId) {
        super(Database.getInstance(), ConnectionsImpl.getInstance());
        details = _details;
        connectionId = _connectionId;
    }

    public void process() {

        String thisUser = database.getUsernameById(connectionId);
        String[] usernames = details.split("\\|");
        String output = "";
        for(String username : usernames){
            if(!database.isBlocked(username, thisUser) && !database.isBlocked(thisUser, username)){
                connections.send(connectionId, "ERROR 8");
                return;
            }
            else{
                output += "ACK 8 " + database.getStats(username) + "\n";
            }
        }
        connections.send(connectionId, output);
    }
}
