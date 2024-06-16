package websocket.messages;

public class Notification extends ServerMessage {
    String Notification;
    public Notification(String Notification) {
        super(ServerMessageType.NOTIFICATION);
        this.Notification = Notification;
    }
}
