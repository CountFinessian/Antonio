package ui;

public class consoleLogger {
    private final String Log;

    public consoleLogger(String serverUrl) {
        this.Log = serverUrl;
    }
    public void run(){
        System.out.println("\uD83D\uDC36 Welcome to the pet store. Sign in to start.");
    }
}
