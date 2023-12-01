package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;

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
            switch (request.getMethod()) {
                case "POST": return create(request);
            }
            // THOUGHT: better 405
            return status(HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    public Response create(Request request)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = null;
        try
        {
            user = objectMapper.readValue(request.getBody(), User.class);
        }
        catch (JsonProcessingException e) {
            return status(HttpStatus.BAD_REQUEST, "JSON invalid");
        }

        try
        {
            user = userService.save(user);
        }
        catch (SQLException e)
        {
            return status(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        String userJson = null;
        try
        {
            userJson = objectMapper.writeValueAsString(user);
        }
        catch (JsonProcessingException e) {
            return status(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return status(HttpStatus.CREATED, userJson);
    }
}
