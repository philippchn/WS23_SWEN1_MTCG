package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.Request;

class AuthorizationHelper
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
}
