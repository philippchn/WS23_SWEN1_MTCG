package at.technikum.apps.mtcg.service;

import at.technikum.server.http.Request;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationTokenHelperTest
{
    @Test
    void tokenUsernameIsNotPathUsername()
    {
        AuthorizationTokenHelper authorizationTokenHelper = new AuthorizationTokenHelper();
        Request request = new Request();
        request.setAuthorizationToken("dummy-mtcgToken");
        assertFalse(authorizationTokenHelper.tokenUsernameIsNotPathUsername(request, "dummy"));
    }
    @Test
    void tokenUsernameIsNotPathUsernameWithWrongUsername()
    {
        AuthorizationTokenHelper authorizationTokenHelper = new AuthorizationTokenHelper();
        Request request = new Request();
        request.setAuthorizationToken("invalid-mtcgToken");
        assertTrue(authorizationTokenHelper.tokenUsernameIsNotPathUsername(request, "dummy"));
    }

    @Test
    void isAdmin()
    {
        AuthorizationTokenHelper authorizationTokenHelper = new AuthorizationTokenHelper();
        Request request = new Request();
        request.setAuthorizationToken("admin-mtcgToken");
        assertTrue(authorizationTokenHelper.isAdmin(request));
    }

    @Test
    void getUsernameFromToken()
    {
        AuthorizationTokenHelper authorizationTokenHelper = new AuthorizationTokenHelper();
        Request request = new Request();
        request.setAuthorizationToken("username-mtcgToken");
        assertEquals(authorizationTokenHelper.getUsernameFromToken(request), "username");
    }

    @Test
    void getUsernameFromTokenWrongUsername()
    {
        AuthorizationTokenHelper authorizationTokenHelper = new AuthorizationTokenHelper();
        Request request = new Request();
        request.setAuthorizationToken("invalid-mtcgToken");
        assertNotEquals(authorizationTokenHelper.getUsernameFromToken(request), "username");
    }
}