package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.SessionService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class SessionController extends Controller {

    private final SessionService sessionService = new SessionService(new UserRepository());

    @Override
    public boolean supports(String route) {
        return route.startsWith("/sessions");
    }

    @Override
    public Response handle(Request request)
    {
        Response response = new Response();

        if (request.getRoute().equals("/sessions")) {
            return switch (request.getMethod()) {
                case "POST" ->  loginUser(request);
                default ->
                        status(HttpStatus.METHOD_NOT_ALLOWED);
            };
        }

        return response;
    }

    private Response loginUser(Request request)
    {
        return sessionService.loginUser(request);
    }

}
