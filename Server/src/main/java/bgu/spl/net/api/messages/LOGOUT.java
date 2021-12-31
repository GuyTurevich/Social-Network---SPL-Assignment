package bgu.spl.net.api.messages;

import bgu.spl.net.api.bidi.Message;

public class LOGOUT implements Message<String> {

    private int connectionId;

    public LOGOUT(int _connectionId) {
        connectionId = _connectionId;
    }

    @Override
    public void process() {


    }
}
