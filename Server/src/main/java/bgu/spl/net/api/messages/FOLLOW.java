package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.Message;

public class FOLLOW implements Message<String> {

    private String details;
    private int connectionId;

    public FOLLOW(String _details, int _connectionId) {
        details = _details;
        connectionId = _connectionId;
    }

    @Override
    public void process() {
        if (details.charAt(0) == '0' || details.charAt(0) == '1') {

            String username = database.getUsernameById(connectionId);
            String usernameToFollow = details.substring(details.indexOf(" ") + 1);

            if(!database.isRegistered(usernameToFollow)) { // User to follow/unfollow isn't registered
                connections.send(connectionId, "ERROR 4");
            }
            else if(details.charAt(0) == '0'){ // FOLLOW
                if(database.isFollowing(username, usernameToFollow)){ //Already Followed
                    connections.send(connectionId, "ERROR 4");
                }
                else{
                    database.follow(username, usernameToFollow);
                    connections.send(connectionId, "ACK 4");
                }
            }
            else{ // UNFOLLOW
                if(!database.isFollowing(username, usernameToFollow)){ //Not Followed
                    connections.send(connectionId, "ERROR 4");
                }
                else{
                    database.unfollow(username, usernameToFollow);
                    connections.send(connectionId, "ACK 4");
                }
            }

        }
        else { // invalid arguments
            connections.send(connectionId, "ERROR 4");
        }
    }
}
