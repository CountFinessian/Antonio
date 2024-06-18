package server.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



@WebSocket
public class WebSocketHandler {

    private Map<Integer, Set<Session>> connections = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthString(), command.getGameID(), session);
            case MAKE_MOVE -> makeMove(command.getAuthString(), command.getGameID(), session, command.getMove());
//            case LEAVE -> leave(command.getAuthString(), session);
//            case RESIGN -> enter(command.getAuthString(), session);
        }
    }

    private void connect(String username, GameData theGame, Session session) throws IOException {
        connections.computeIfAbsent(theGame.gameID(), k -> new HashSet<>()).add(session);


        var loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, theGame.gameName(), theGame);
        session.getRemote().sendString(new Gson().toJson(loadGameMessage));
        String notif = new String ( username  + "joined the game!" );;
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, notif, null);
        broadcast(theGame.gameID(), session, notification);
    }

    private void makeMove(String username, GameData theGame, Session session, ChessMove move) throws IOException {
        var loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, theGame.gameName(), theGame);
        session.getRemote().sendString(new Gson().toJson(loadGameMessage));
        broadcast(theGame.gameID(), session, loadGameMessage);

        var message = String.format(username + "made move" + move);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, theGame);
        broadcast(theGame.gameID(), session, notification);
    }


    private void broadcast(int gameID, Session rootClientSession, ServerMessage notification) throws IOException {
        Set<Session> sessions = connections.get(gameID);
        if (sessions != null) {
            String message = new Gson().toJson(notification);
            for (Session s : sessions) {
                if (s.isOpen() && !s.equals(rootClientSession)) {
                    s.getRemote().sendString(message);
                }
            }
        }
    }
}