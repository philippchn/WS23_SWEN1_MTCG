package at.technikum;

import at.technikum.display.DisplayApp;
import at.technikum.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(new DisplayApp());
    }
}