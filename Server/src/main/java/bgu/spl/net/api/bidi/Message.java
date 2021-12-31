package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.Database;

public interface Message<T> {

    public ConnectionsImpl connections = ConnectionsImpl.getInstance();
    public Database database = Database.getInstance();

    public void process();
}
