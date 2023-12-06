package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;
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

    private final UserService userService = new UserService();

    private final ObjectMapper objectMapper = new ObjectMapper();

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
                case "GET" -> readAll();
                case "DELETE" -> deleteAll();
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

    private Response deleteAll()
    {
        try
        {
            userService.deleteAll();
        }
        catch (SQLException e)
        {
            return statusCustomBody(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return status(HttpStatus.OK);
    }

    private User jsonToUserObject(Request request) throws JsonProcessingException
    {
        return objectMapper.readValue(request.getBody(), User.class);
    }

    private UserData jsonToUserDataObject(Request request) throws JsonProcessingException
    {
        return objectMapper.readValue(request.getBody(), UserData.class);
    }

    private Response create(Request request)
    {
        User user;
        try
        {
            user = jsonToUserObject(request);
        }
        catch (JsonProcessingException e)
        {
            return statusCustomBody(HttpStatus.BAD_REQUEST, "JSON invalid");
        }

        try
        {
            userService.save(user);
        }
        catch (SQLException e)
        {
            if (Objects.equals(e.getSQLState(), "23505"))
            {
                return status(HttpStatus.CONFLICT);
            }
            return statusCustomBody(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return status(HttpStatus.CREATED);
    }

    private Response readAll()
    {
        List<User> users;
        try
        {
           users = userService.findAll();
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

    private Response getUserDataByUsername(Request request)
    {
        Optional<UserData> userData;
        try
        {
            String username = request.getRoute().replace("/users/", "");
            userData = userService.getUserDataByUsername(username);
        }
        catch (SQLException e)
        {
            return statusCustomBody(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        if (userData.isEmpty())
        {
            return statusCustomBody(HttpStatus.NOT_FOUND, "User not found");
        }
        String userJson;
        try
        {
            userJson = objectMapper.writeValueAsString(userData.get());
        }
        catch (JsonProcessingException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return statusCustomBody(HttpStatus.OK, userJson);
    }

    private Response updateUser(Request request)
    {
        UserData userData;
        try
        {
            userData = jsonToUserDataObject(request);
        }
        catch (JsonProcessingException e)
        {
            return statusCustomBody(HttpStatus.BAD_REQUEST, "JSON invalid");
        }

        String username = request.getRoute().replace("/users/", "");
        try
        {
            userService.updateUserDataByUsername(username, userData);
        }
        catch (SQLException e)
        {
            if (e.getSQLState().equals("23503"))
            {
                return statusCustomBody(HttpStatus.NOT_FOUND, "User not found");
            }
            return statusCustomBody(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return status(HttpStatus.OK);
    }
}
