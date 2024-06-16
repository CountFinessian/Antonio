package ui.websocket;

import com.google.gson.Gson;
import exception.DataAccessException;
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


    public WebSocketFacade(String url) throws DataAccessException, DataFormatException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            notificationHandler = new NotificationHandler();

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
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

    public void CONNNECT(String visitorName) throws IOException {
        try {
            var action = new UserGameCommand(visitorName, UserGameCommand.CommandType.CONNECT, 5);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void MAKE_MOVE(String visitorName) throws IOException {
        try {
            var action = new UserGameCommand(visitorName, UserGameCommand.CommandType.MAKE_MOVE, 5);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void RESIGN(String visitorName) throws IOException {
        try {
            var action = new UserGameCommand(visitorName, UserGameCommand.CommandType.RESIGN, 5);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void LEAVE(String visitorName) throws IOException {
        try {
            var action = new UserGameCommand(visitorName, UserGameCommand.CommandType.LEAVE, 5);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

}

