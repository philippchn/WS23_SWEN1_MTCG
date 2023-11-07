package at.technikum.server;

public class Server {
    private final ServerApplication serverApplication;

    private Object serverSocket;

    public Server(ServerApplication serverApplication)
    {
        this.serverApplication = serverApplication;
    }

    public void start()
    {
        System.out.println("Server gestartet");
        RequestHandler requestHandler = new RequestHandler(null, serverApplication);
        requestHandler.handle();
    }
}
