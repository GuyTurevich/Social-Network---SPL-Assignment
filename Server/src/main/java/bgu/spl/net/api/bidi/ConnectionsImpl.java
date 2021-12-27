package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.NonBlockingConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConnectionsImpl<T> singleton;
    ConcurrentHashMap<Integer, NonBlockingConnectionHandler <T>> clients;

    public ConnectionsImpl(){
        clients = new ConcurrentHashMap<Integer,NonBlockingConnectionHandler<T>>();
    }

    public  ConnectionsImpl getInstance() {
        if (singleton == null) singleton = new ConnectionsImpl<T>();
        return singleton;
    }

    @Override
    public boolean send(int connectionId, T msg) {
        if(!clients.containsKey(connectionId))  return false;
        else{

        }
        return false; // should delete after done implementing
    }

    @Override
    public void broadcast(T msg) {

    }

    @Override
    public void disconnect(int connectionId) {

    }
}
