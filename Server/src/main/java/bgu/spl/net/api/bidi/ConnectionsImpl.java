package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.NonBlockingConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    ConcurrentHashMap<Integer, NonBlockingConnectionHandler <T>> clients;

    @Override
    public boolean send(int connectionId, T msg) {
        return false;
    }

    @Override
    public void broadcast(T msg) {

    }

    @Override
    public void disconnect(int connectionId) {

    }
}
