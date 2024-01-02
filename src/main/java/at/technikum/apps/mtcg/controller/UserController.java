package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class UserController extends Controller {
    private final UserService userService = new UserService(new UserRepository());

    @Override
    public boolean supports(String route) {
        return route.startsWith("/users");
    }

    @Override
    public Response handle(Request request)
    {
        if (request.getRoute().equals("/users"))
        {
            return switch (request.getMethod()) {
                case "POST" -> create(request);
                case "GET" -> findAll();
                default ->
                        status(HttpStatus.METHOD_NOT_ALLOWED);
            };
        }
        else
        {
            return switch (request.getMethod()) {
                case "GET" -> getUserDataByUsername(request);
                case "PUT" -> updateUser(request);
                default ->
                        status(HttpStatus.METHOD_NOT_ALLOWED);
            };
        }
    }

    private Response create(Request request)
    {
        return userService.create(request);
    }

    private Response findAll()
    {
        return userService.findAll();
    }

    private Response getUserDataByUsername(Request request)
    {
        return userService.getUserDataByUsername(request);
    }

    private Response updateUser(Request request)
    {
        return userService.updateUser(request);
    }
}
