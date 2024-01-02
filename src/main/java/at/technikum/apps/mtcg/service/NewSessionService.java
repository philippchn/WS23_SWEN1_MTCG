package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NewSessionService
{
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public NewSessionService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public Response loginUser(Request request)
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

        Optional<Token> token = getTokenFromUser(user);
        if (token.isEmpty())
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        String tokenJson;
        try
        {
            tokenJson = objectMapper.writeValueAsString(token.get());
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseHelper.statusJsonBody(HttpStatus.OK, tokenJson);
    }

    private Optional<Token> getTokenFromUser(User userFromRequest)
    {
        try
        {
            Optional<User> userFromDB = userRepository.findUserByUsername(userFromRequest.Username());

            if (userFromDB.isEmpty())
            {
                return Optional.empty();
            }

            if (userFromDB.get().Password().equals(userFromRequest.Password()))
            {
                userRepository.loginUser(userFromDB.get().Username());
                return userRepository.getTokenOfUser(userFromDB.get().Username());
            }
            return Optional.empty();
        }
        catch (SQLException e)
        {
            return Optional.empty();
        }
    }
}
