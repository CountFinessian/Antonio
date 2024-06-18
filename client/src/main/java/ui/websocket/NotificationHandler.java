package ui.websocket;

import chess.InvalidMoveException;
import ui.GamePlay;
import websocket.messages.*;
import com.google.gson.Gson;

import java.io.IOException;

public class NotificationHandler {
    private Gson gson = new Gson(); // Initialize Gson instance
    public GamePlay game;

    public NotificationHandler(GamePlay game) {
        this.game = game;
    }

    public void routeMessages(ServerMessage msg) throws IOException, InvalidMoveException {
        // Check if the server message type is LOAD_GAME
        if (msg.serverMessageType == ServerMessage.ServerMessageType.LOAD_GAME) {
            handleLoadGame(msg);
        }
        if (msg.serverMessageType == ServerMessage.ServerMessageType.ERROR) {
            handleError(msg);
        }
        if (msg.serverMessageType == ServerMessage.ServerMessageType.NOTIFICATION) {
            handleNotification(msg);
        }
    }

    public void handleLoadGame(ServerMessage loadGameMsg) throws IOException, InvalidMoveException {
        String output = loadGameMsg.getMessage();
        System.out.println(output);
        game.displayboard(loadGameMsg.getGameData().game().getBoard(), null);
    }

    public void handleError(ServerMessage errorMsg) {}

    public void handleNotification(ServerMessage notificationMsg) {}



}
