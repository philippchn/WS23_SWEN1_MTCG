package at.technikum.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket server;

    private final ServerApplication serverApplication;

    public Server(ServerApplication serverApplication) {
        this.serverApplication = serverApplication;
    }

    public void start() {
        try {
            server = new ServerSocket(10001);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Server started on http://localhost:10001");

        while (true) {
            try {
                Socket socket = server.accept();

                RequestHandler handler = new RequestHandler(socket, serverApplication);
                handler.handle();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
