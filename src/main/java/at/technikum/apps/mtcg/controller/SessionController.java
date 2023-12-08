package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.SessionService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.Optional;

public class SessionController extends Controller {

    private final SessionService sessionService = new SessionService(new UserRepository());

    private final ObjectMapper objectMapper = new ObjectMapper();

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
                case "POST" ->  getToken(request);
                default ->
                        status(HttpStatus.METHOD_NOT_ALLOWED);
            };
        }

        return response;
    }

    private User jsonToUserRequest(Request request) throws JsonProcessingException {
        return objectMapper.readValue(request.getBody(), User.class);
    }

    private Response getToken(Request request)
    {
        User user;
        try
        {
            user = jsonToUserRequest(request);
        }
        catch (JsonProcessingException e)
        {
            return statusCustomBody(HttpStatus.BAD_REQUEST, "JSON invalid");
        }

        Optional<Token> token;
        try
        {
            token = sessionService.getToken(user);
        }
        catch (SQLException e)
        {
            return statusCustomBody(HttpStatus.NOT_FOUND, "User not found");
        }

        if (token.isEmpty())
        {
            return statusCustomBody(HttpStatus.UNAUTHORIZED, "Invalid username/password provided");
        }

        String tokenJson;
        try
        {
            tokenJson = objectMapper.writeValueAsString(token.get());
        }
        catch (JsonProcessingException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return statusCustomBody(HttpStatus.OK, tokenJson);
    }

}
