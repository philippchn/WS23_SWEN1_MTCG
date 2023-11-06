package at.technikum.server.http;

public class Request {
    enum Method
    {
        GET,
        POST,
        PUT,
        DELETE
    }

    private Method method;
    private String route;
    private String contentType;
    private String body;
}
