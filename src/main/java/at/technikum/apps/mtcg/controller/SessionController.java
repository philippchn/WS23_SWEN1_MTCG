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

        switch(request.getMethod())
        {
            case "POST": return status(HttpStatus.OK);
        }

        // THOUGHT: better 405
        return status(HttpStatus.BAD_REQUEST);
    }

    void mapToTokenRequest(String username, String password)
    {

    }
}
