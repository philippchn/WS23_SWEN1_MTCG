package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.NewUserService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserController extends Controller {
    private final NewUserService newUserService = new NewUserService(new UserRepository());

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
        return newUserService.create(request);
    }

    private Response findAll()
    {
        return newUserService.findAll();
    }

    private Response getUserDataByUsername(Request request)
    {
        return newUserService.getUserDataByUsername(request);
    }

    private Response updateUser(Request request)
    {
        return newUserService.updateUser(request);
    }
}
