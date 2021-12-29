package bgu.spl.net.api.bidi;

import bgu.spl.net.api.messages.REGISTER;
import bgu.spl.net.srv.Database;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<String> {

    Database database;

    @Override
    public void start(int connectionId, Connections<String> connections) {
        connections.connect(connectionId);
        database = database.getInstance();
    }

    @Override
    public void process(String message) {
        int spaceIndex = message.indexOf(" ");
        String command, details = null;
        if (spaceIndex == -1) command = message;
        else {
            command = message.substring(0,spaceIndex);
            details = message.substring(spaceIndex+1);
        }


        if(command.equals("REGISTER"))
            new REGISTER(details, database).process();

    }

    @Override
    public boolean shouldTerminate() {
        return false;// should delete after done implementing
    }
}
