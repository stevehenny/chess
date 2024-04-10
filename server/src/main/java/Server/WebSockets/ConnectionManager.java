package server.WebSockets;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
public class ConnectionManager {

public final ConcurrentHashMap<String, Connections> connections = new ConcurrentHashMap<>();

public void add(String playerName, Session session) {
        connections.put(playerName, new Connections(playerName, session));
    }

public void remove(String playerName) {
        connections.remove(playerName);
    }

    public void broadcast(String excludeVisitorName, int gameId, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connections>();
        for(var entry: connections.values()){
            if(entry.session.isOpen()){
                if(!entry.visitorName.equals(excludeVisitorName)){
                    entry.send(new Gson().toJson(notification));
                }
            } else{
                removeList.add(entry);
            }
        }
        for(var entry: removeList){
            connections.remove(entry.visitorName);
        }
    }
}