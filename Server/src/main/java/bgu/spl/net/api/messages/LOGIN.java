package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;

public class LOGIN implements Message<String> {

    private String details;
    private Database database;

    public LOGIN(String _details, Database _database) {
        details = _details;
        database = _database;
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
            // send ACK
        }

    }
}
