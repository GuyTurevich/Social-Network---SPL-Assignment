package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.Message;
import bgu.spl.net.srv.Database;

public class LOGOUT implements Message<String> {

    private String details;
    private Database database;

    public LOGOUT(String _details, Database _database) {
        details = _details;
        database = _database;
    }

    @Override
    public void process() {


    }
}
