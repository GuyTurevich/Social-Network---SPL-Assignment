package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> clientsByIds;

    private static class SingletonHolder{
        private static ConnectionsImpl instance = new ConnectionsImpl();
    }

    public ConnectionsImpl(){
        clientsByIds = new ConcurrentHashMap<Integer,ConnectionHandler<T>>();
    }

    public static ConnectionsImpl getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public boolean send(int connectionId, T msg) {
        if(!clientsByIds.containsKey(connectionId))  return false;
        else{
            clientsByIds.get(connectionId).send(msg);
            return true;
        }
    }

    @Override
    public void broadcast(T msg) {
        for(Map.Entry<Integer, ConnectionHandler<T>> entry : clientsByIds.entrySet()){
            entry.getValue().send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        clientsByIds.remove(connectionId);
    }

    public void connect(int connectionId, ConnectionHandler<T> connectionHandler){
        clientsByIds.put(connectionId , connectionHandler);
    }
}
