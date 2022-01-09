package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;
import bgu.spl.net.srv.User;

public class REGISTER extends Message<String> {

    private String details;
    private int connectionId;

    public REGISTER(String _details, int _connectionId) {
        super(Database.getInstance(), ConnectionsImpl.getInstance());
        details = _details;
        connectionId = _connectionId;
    }


    public synchronized void process() {
        String[] arguments = details.split(" ");
        if (arguments.length != 3 || arguments[2].length() != 10) {
            connections.send(connectionId, "ERROR 1");
            return;
        }
        User user = new User(arguments[0], arguments[1], arguments[2]);

        if (database.isRegistered(user.getUsername())) {
            connections.send(connectionId, "ERROR 1");
        }
        else {
            database.addUser(user);
            connections.send(connectionId, "ACK 1");
        }
    }
}
