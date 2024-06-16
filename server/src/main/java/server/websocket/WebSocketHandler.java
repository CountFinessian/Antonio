package server.websocket;

import com.google.gson.Gson;
import exception.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@WebSocket
public class WebSocketHandler {

   Map<Integer, Set<Session>> connections = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connnect(command.getGameID(), session);
//            case MAKE_MOVE -> enter(command.getAuthString(), session);
//            case LEAVE -> enter(command.getAuthString(), session);
//            case RESIGN -> enter(command.getAuthString(), session);
        }
    }

    private void connect(int gameID, Session session) throws IOException {
        connections.put(gameID, (Set<Session>) session);
        //send the message.
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(visitorName, notification);
    }

    private void make_move(String visitorName) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(visitorName, notification);
    }

    public void leave(String petName, String sound) throws DataAccessException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void resign(String petName, String sound) throws DataAccessException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}