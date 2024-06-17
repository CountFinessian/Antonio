package websocket.messages;

import model.GameData;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */




public class ServerMessage {

    public ServerMessageType serverMessageType;
    String label;
    GameData gameData;

    // TODO add chess game as field here, will normally be null except for LOAD_GAME will actually have a value for it


    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type, String label, GameData gameData) {
        this.serverMessageType = type;
        this.label = label;
        this.gameData = gameData;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public String getMessage() {
        if (this.serverMessageType == ServerMessageType.LOAD_GAME) {
            return "You have the game called " + label;
        }
        if (this.serverMessageType == ServerMessageType.ERROR) {
            return "Error: " + label;
        }
        if (this.serverMessageType == ServerMessageType.NOTIFICATION) {
            return label;
        }
        return "Unknown message";
    }

    public GameData getGameData() {
        if (this.serverMessageType == ServerMessageType.LOAD_GAME) {
            return gameData;
        }
        return null;
    }
}

