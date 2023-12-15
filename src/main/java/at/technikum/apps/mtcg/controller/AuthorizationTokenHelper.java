package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.Request;

class AuthorizationTokenHelper
{
    static boolean invalidToken(Request request, String username)
    {
        if (request.getAuthorizationToken().equals("admin-mtcgToken"))
        {
            return false;
        }
        if (request.getAuthorizationToken().equals(username + "-mtcgToken"))
        {
            return false;
        }
        return true;
    }

    static boolean isAdmin(Request request)
    {
        return request.getAuthorizationToken().equals("admin-mtcgToken");
    }

    static boolean isUser(Request request, String username)
    {
        return request.getAuthorizationToken().equals(username + "-mtcgToken");
    }

    static String getUsernameFromToken(Request request)
    {
        return request.getAuthorizationToken().replace("-mtcgToken", "");
    }
}
