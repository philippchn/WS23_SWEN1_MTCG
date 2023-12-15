package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;
import at.technikum.apps.mtcg.repository.UserRepository;
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

    private final UserService userService = new UserService(new UserRepository());

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

    private Response create(Request request)
    {
        User user;
        try
        {
            user = objectMapper.readValue(request.getBody(), User.class);
        }
        catch (JsonProcessingException e)
        {
            return status(HttpStatus.BAD_REQUEST);
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
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
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

           return statusJsonBody(HttpStatus.OK, usersJson);
        }
        catch (SQLException | JsonProcessingException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Response getUserDataByUsername(Request request)
    {
        String username = request.getRoute().replace("/users/", "");
        if (AuthorizationTokenHelper.invalidToken(request, username))
        {
            return status(HttpStatus.UNAUTHORIZED);
        }

        Optional<UserData> userData;
        try
        {
            userData = userService.getUserDataByUsername(username);
        }
        catch (SQLException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (userData.isEmpty())
        {
            return status(HttpStatus.NOT_FOUND);
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
        return statusJsonBody(HttpStatus.OK, userJson);
    }

    private Response updateUser(Request request)
    {
        String username = request.getRoute().replace("/users/", "");
        if (AuthorizationTokenHelper.invalidToken(request, username))
        {
            return status(HttpStatus.UNAUTHORIZED);
        }

        UserData userData;
        try
        {
            userData = objectMapper.readValue(request.getBody(), UserData.class);
        }
        catch (JsonProcessingException e)
        {
            return status(HttpStatus.BAD_REQUEST);
        }

        try
        {
            userService.updateUserDataByUsername(username, userData);
        }
        catch (SQLException e)
        {
            if (e.getSQLState().equals("23503"))
            {
                return status(HttpStatus.NOT_FOUND);
            }
            return status(HttpStatus.BAD_REQUEST);
        }
        return status(HttpStatus.OK);
    }

    private Response deleteAll()
    {
        try
        {
            userService.deleteAll();
        }
        catch (SQLException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return status(HttpStatus.OK);
    }
}
