package bgu.spl.net.api.bidi;

import bgu.spl.net.api.messages.*;
import bgu.spl.net.srv.Database;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<String> {

    Database database;
    ConnectionsImpl<String> connections ;
    int connectionId;
    boolean terminate;

    public BidiMessagingProtocolImpl(Database _database){
        database = _database;
        terminate = false;
    }

    @Override
    public void start(int connectionId, Connections<String> connections) {
        this.connectionId = connectionId;
        this.connections = (ConnectionsImpl<String>) connections;
    }

    @Override
    public void process(String message) {
        if(!terminate) {
            int spaceIndex = message.indexOf(" ");
            String command, details = null;
            if (spaceIndex == -1) command = message;
            else {
                command = message.substring(0, spaceIndex);
                details = message.substring(spaceIndex + 1);
            }


            if (command.equals("REGISTER")) new REGISTER(details, connectionId).process();

            else if (command.equals("LOGIN")) new LOGIN(details, connectionId).process();

            else if (!database.isLoggedIn(database.getUsernameById(connectionId))) {
                // Send ERROR - No user is logged in
            } else if (command.equals("LOGOUT")) {
                LOGOUT logout = new LOGOUT(connectionId, this);
                logout.process();
                if (logout.hasLoggedOut()) shouldTerminate();
            } else if (command.equals("FOLLOW")) new FOLLOW(details, connectionId).process();

            else if (command.equals("POST")) new POST(details, connectionId).process();

            else if (command.equals("PM")) new PM(details, connectionId).process();

            else if (command.equals("LOGSTAT")) new LOGSTAT(connectionId).process();

            else if (command.equals("STAT")) new STAT(details, connectionId).process();

            else if (command.equals("BLOCK")) new BLOCK(details, connectionId).process();
        }
    }


    @Override
    public boolean shouldTerminate() {
        return terminate = true;
    }
}
