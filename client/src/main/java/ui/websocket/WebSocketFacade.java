package ui.websocket;

import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.DataAccessException;
import model.GameData;
import ui.GamePlay;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.zip.DataFormatException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    GamePlay game;


    public WebSocketFacade(String url, GamePlay game) throws DataAccessException, DataFormatException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            notificationHandler = new NotificationHandler(game);

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage msg = new Gson().fromJson(message, ServerMessage.class);
                    try {
                        notificationHandler.routeMessages(msg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidMoveException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataFormatException(ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String username, GameData theGame) throws IOException {
        try {
            var action = new UserGameCommand(username, UserGameCommand.CommandType.CONNECT, theGame, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void makeMove(ChessMove move, GameData theGame, String username) throws IOException {
        try {
            var action = new UserGameCommand(username, UserGameCommand.CommandType.MAKE_MOVE, theGame, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

//    public void RESIGN(String visitorName) throws IOException {
//        try {
//            var action = new UserGameCommand(visitorName, UserGameCommand.CommandType.RESIGN, 5);
//            this.session.getBasicRemote().sendText(new Gson().toJson(action));
//        } catch (IOException ex) {
//            throw new IOException(ex.getMessage());
//        }
//    }
//
    public void leave(String visitorName, GameData theGame) throws IOException {
        try {
            var action = new UserGameCommand(visitorName, UserGameCommand.CommandType.LEAVE, theGame, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

}

