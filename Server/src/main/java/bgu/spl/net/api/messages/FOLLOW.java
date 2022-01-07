package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;

public class FOLLOW extends Message<String> {

    private String details;
    private int connectionId;

    public FOLLOW(String _details, int _connectionId) {
        super(Database.getInstance(), ConnectionsImpl.getInstance());
        details = _details;
        connectionId = _connectionId;
    }


    public void process() {

        String username = database.getUsernameById(connectionId);
        String[] arguments = details.split(" ");
        if (arguments.length != 2 || (!arguments[0].equals("0") && !arguments[0].equals("1"))) {
            connections.send(connectionId, "ERROR 2");
            return;
        }
        String usernameToFollow = arguments[1];

        if (!database.isRegistered(usernameToFollow) ||
                database.isBlocked(username, usernameToFollow) ||
                database.isBlocked(usernameToFollow, username) ||
                username.equals(usernameToFollow)) { // User to follow/unfollow isn't registered or try to follow itself
            connections.send(connectionId, "ERROR 4");
        } else if (arguments[0].equals("0")) { // FOLLOW
            if (database.isFollowing(username, usernameToFollow)) { //Already Followed
                connections.send(connectionId, "ERROR 4");
            } else {
                database.follow(username, usernameToFollow);
                connections.send(connectionId, "ACK 4 0 " + usernameToFollow);
            }
        } else { // UNFOLLOW
            if (!database.isFollowing(username, usernameToFollow)) { //Not Followed
                connections.send(connectionId, "ERROR 4");
            } else {
                database.unfollow(username, usernameToFollow);
                connections.send(connectionId, "ACK 4 1 " + usernameToFollow);
            }
        }


    }
}
