package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.SessionService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.Request;

import java.sql.SQLException;
import java.util.Optional;

class AuthorizationTokenHelper
{
    private static final SessionService sessionService = new SessionService(new UserRepository());

    static boolean invalidToken(Request request)
    {
        String username = getUsernameFromToken(request);
        if (request.getAuthorizationToken().equals("admin-mtcgToken"))
        {
            return false;
        }
        try
        {
            Optional<Token> token = sessionService.getTokenOfUser(username);
            if (token.isEmpty())
            {
                return true;
            }
        }
        catch (SQLException e)
        {
            return true;
        }
        return false;
    }

    static boolean tokenUsernameIsNotPathUsername(Request request, String username)
    {
        return !username.equals(getUsernameFromToken(request));
    }

    static boolean isAdmin(Request request)
    {
        return request.getAuthorizationToken().equals("admin-mtcgToken");
    }

    static String getUsernameFromToken(Request request)
    {
        return request.getAuthorizationToken().replace("-mtcgToken", "");
    }
}
