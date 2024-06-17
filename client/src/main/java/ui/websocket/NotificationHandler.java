package ui.websocket;

import chess.InvalidMoveException;
import chess.PAWN;
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

    public void RouteMessages(ServerMessage msg) throws IOException, InvalidMoveException {
        // Check if the server message type is LOAD_GAME
        if (msg.serverMessageType == ServerMessage.ServerMessageType.LOAD_GAME) {
            HandleLoadGame(msg);
        }
        if (msg.serverMessageType == ServerMessage.ServerMessageType.ERROR) {
            HandleError(msg);
        }
        if (msg.serverMessageType == ServerMessage.ServerMessageType.NOTIFICATION) {
            HandleNotification(msg);
        }
    }

    public void HandleLoadGame(ServerMessage loadGameMsg) throws IOException, InvalidMoveException {
        String output = loadGameMsg.getMessage();
        System.out.println(output);
        game.displayboard(loadGameMsg.getGameData().game().getBoard(), null);
    }

    public void HandleError(ServerMessage errorMsg) {}

    public void HandleNotification(ServerMessage notificationMsg) {}



}
