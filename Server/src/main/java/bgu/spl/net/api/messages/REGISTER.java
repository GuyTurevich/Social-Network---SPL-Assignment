package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

public class REGISTER implements Message<String> {

    private String details;
    private Database database;


    public REGISTER(String _details, Database _database) {
        database = _database;
        details = _details;
    }

    @Override
    public void process() {
        int space1 = details.indexOf(" ");
        int space2 = details.lastIndexOf(" ");
        User user = new User(details.substring(0, space1),
                details.substring(space1 + 1, space2),
                details.substring(space2 + 1, space2 + 11));
        if (database.isRegistered(user.getUsername())) {
            //SEND ERROR
        } else {
            database.addUser(user);
            // SEND ACK
        }
    }
}
