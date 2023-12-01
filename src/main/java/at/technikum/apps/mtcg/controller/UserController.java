package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;

public class UserController extends Controller {

    private final UserService userService;

    public UserController()
    {
        this.userService = new UserService();
    }

    @Override
    public boolean supports(String route) {
        return route.equals("/users");
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response();

        if (request.getRoute().equals("/users")) {
            return switch (request.getMethod()) {
                case "POST" -> create(request);
                case "GET" -> readAll();
                default ->
                        status(HttpStatus.METHOD_NOT_ALLOWED);
            };
        }

        return response;
    }

    public Response create(Request request)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        User user;
        try
        {
            user = objectMapper.readValue(request.getBody(), User.class);
        }
        catch (JsonProcessingException e) {
            return statusCustomBody(HttpStatus.BAD_REQUEST, "JSON invalid");
        }

        try
        {
            user = userService.save(user);
        }
        catch (SQLException e)
        {
            return statusCustomBody(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        String userJson;
        try
        {
            userJson = objectMapper.writeValueAsString(user);
        }
        catch (JsonProcessingException e) {
            return statusCustomBody(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return statusCustomBody(HttpStatus.CREATED, userJson);
    }

    public Response readAll()
    {
        List<User> users;
        try
        {
           users = userService.findAll();
           ObjectMapper objectMapper = new ObjectMapper();
           String usersJson = objectMapper.writeValueAsString(users);

           return statusCustomBody(HttpStatus.OK, usersJson);
        }
        catch (SQLException e)
        {
            return statusCustomBody(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        catch(JsonProcessingException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
