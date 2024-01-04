package at.technikum.apps.mtcg.service;

import at.technikum.server.http.Request;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationTokenHelperTest
{
    @Test
    void tokenUsernameIsNotPathUsername()
    {
        Request request = new Request();
        request.setAuthorizationToken("dummy-mtcgToken");
        assertFalse(AuthorizationTokenHelper.tokenUsernameIsNotPathUsername(request, "dummy"));
    }
    @Test
    void tokenUsernameIsNotPathUsernameWithWrongUsername()
    {
        Request request = new Request();
        request.setAuthorizationToken("invalid-mtcgToken");
        assertTrue(AuthorizationTokenHelper.tokenUsernameIsNotPathUsername(request, "dummy"));
    }

    @Test
    void isAdmin()
    {
        Request request = new Request();
        request.setAuthorizationToken("admin-mtcgToken");
        assertTrue(AuthorizationTokenHelper.isAdmin(request));
    }

    @Test
    void getUsernameFromToken()
    {
        Request request = new Request();
        request.setAuthorizationToken("username-mtcgToken");
        assertEquals(AuthorizationTokenHelper.getUsernameFromToken(request), "username");
    }

    @Test
    void getUsernameFromTokenWrongUsername()
    {
        Request request = new Request();
        request.setAuthorizationToken("invalid-mtcgToken");
        assertNotEquals(AuthorizationTokenHelper.getUsernameFromToken(request), "username");
    }
}