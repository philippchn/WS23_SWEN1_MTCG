package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.controller.AuthorizationTokenHelper;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class NewUserService
{
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public NewUserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public Response create(Request request)
    {
        User user;
        try
        {
            user = objectMapper.readValue(request.getBody(), User.class);
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.BAD_REQUEST);
        }

        try
        {
            userRepository.saveUser(user);
        }
        catch (SQLException e)
        {
            if (Objects.equals(e.getSQLState(), "23505"))
            {
                return ResponseHelper.status(HttpStatus.CONFLICT);
            }
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseHelper.status(HttpStatus.CREATED);
    }

    public Response findAll()
    {
        List<User> users;
        try
        {
            users = userRepository.findAll();
        }
        catch (SQLException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try
        {
            String usersJson = objectMapper.writeValueAsString(users);
            return ResponseHelper.statusJsonBody(HttpStatus.OK, usersJson);
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Response getUserDataByUsername(Request request)
    {
        String username = request.getRoute().replace("/users/", "");
        if (AuthorizationTokenHelper.tokenUsernameIsNotPathUsername(request, username))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }
        if (AuthorizationTokenHelper.invalidToken(request))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        Optional<UserData> userData;
        try
        {
            userData = userRepository.findUserDataByUsername(username);
        }
        catch (SQLException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (userData.isEmpty())
        {
            return ResponseHelper.status(HttpStatus.NOT_FOUND);
        }
        String userJson;
        try
        {
            userJson = objectMapper.writeValueAsString(userData.get());
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseHelper.statusJsonBody(HttpStatus.OK, userJson);
    }

    public Response updateUser(Request request)
    {
        String username = request.getRoute().replace("/users/", "");
        if (AuthorizationTokenHelper.tokenUsernameIsNotPathUsername(request, username))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }
        if (AuthorizationTokenHelper.invalidToken(request))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }


        UserData userData;
        try
        {
            userData = objectMapper.readValue(request.getBody(), UserData.class);
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.BAD_REQUEST);
        }

        try
        {
            userRepository.updateUserDataByUsername(username, userData);
        }
        catch (SQLException e)
        {
            if (e.getSQLState().equals("23503"))
            {
                return ResponseHelper.status(HttpStatus.NOT_FOUND);
            }
            return ResponseHelper.status(HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.status(HttpStatus.OK);
    }
}
