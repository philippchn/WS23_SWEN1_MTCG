package at.technikum.server;

public class RequestHandler {
    private final Object client;
    private final ServerApplication serverApplication;
    RequestHandler(Object client, ServerApplication serverApplication)
    {
        this.client = client;
        this.serverApplication = serverApplication;
    }

    public void handle() {

    }
}
