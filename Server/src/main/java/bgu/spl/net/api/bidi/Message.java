package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.Database;

public class Message<T> {

    protected ConnectionsImpl connections;
    protected Database database;

    public Message(Database _database, ConnectionsImpl _connections) {
        connections = _connections;
        database = _database;
    }
}
