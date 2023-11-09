package at.technikum.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerApplication serverApplication;

    private ServerSocket serverSocket;

    public Server(ServerApplication serverApplication)
    {
        this.serverApplication = serverApplication;
    }

    public void start()
    {
        try
        {
            serverSocket = new ServerSocket(10001);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println("Server running");

        while(true)
        {
            try
            {
                Socket socket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(socket, serverApplication);
                requestHandler.handle();
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
