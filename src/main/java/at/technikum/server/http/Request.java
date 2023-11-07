package at.technikum.server.http;

public class Request {
    private HttpMethod method;
    private String route;
    private ContentType contentType;
    private int contentLength;
    private String body;
}
