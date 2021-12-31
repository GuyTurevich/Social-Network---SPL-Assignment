package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> activeClients;

    private static class SingletonHolder{
        private static ConnectionsImpl instance = new ConnectionsImpl();
    }

    public ConnectionsImpl(){
        activeClients = new ConcurrentHashMap<Integer,ConnectionHandler<T>>();
    }

    public static ConnectionsImpl getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public boolean send(int connectionId, T msg) {
        if(!activeClients.containsKey(connectionId))  return false;
        else{

        }
        return false; // should delete after done implementing
    }

    @Override
    public void broadcast(T msg) {

    }

    @Override
    public void disconnect(int connectionId) {
        activeClients.remove(connectionId);
    }

    public void connect(int connectionId){
        activeClients.put(connectionId , null); // *need to figure out how to get its connectionHandler
    }
}
