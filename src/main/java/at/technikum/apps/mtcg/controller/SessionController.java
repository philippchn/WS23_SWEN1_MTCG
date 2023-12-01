package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class SessionController extends Controller {
    @Override
    public boolean supports(String route) {
        return route.startsWith("/sessions");
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response();

        if (request.getRoute().equals("/sessions")) {
            return switch (request.getMethod()) {
                case "POST" ->  status(HttpStatus.OK);
                default ->
                        status(HttpStatus.METHOD_NOT_ALLOWED);
            };
        }

        return response;
    }

    void mapToTokenRequest(String username, String password)
    {

    }
}
