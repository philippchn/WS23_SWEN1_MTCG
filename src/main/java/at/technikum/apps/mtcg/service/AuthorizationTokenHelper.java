package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.Request;

import java.util.Optional;

public class AuthorizationTokenHelper
{
    private final UserRepository userRepository = new UserRepository();

    boolean invalidToken(Request request)
    {
        String username = getUsernameFromToken(request);
        if (request.getAuthorizationToken().equals("admin-mtcgToken"))
        {
            return false;
        }

        Optional<Token> token = userRepository.getTokenOfUser(username);
        return token.isEmpty();
    }

    boolean tokenUsernameIsNotPathUsername(Request request, String username)
    {
        return !username.equals(getUsernameFromToken(request));
    }

    boolean isAdmin(Request request)
    {
        return request.getAuthorizationToken().equals("admin-mtcgToken");
    }

    String getUsernameFromToken(Request request)
    {
        return request.getAuthorizationToken().replace("-mtcgToken", "");
    }
}
