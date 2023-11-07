package at.technikum.server;

import at.technikum.server.http.Request;

public interface ServerApplication {
    void handle(Request request);
}
