package at.technikum.apps.mtcg.controller;

import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public abstract class Controller {

    public abstract boolean supports(String route);

    public abstract Response handle(Request request);

    /**
     * Return a Response using a httpStatus Code.
     * The User will get the default error message of the status code as response.
     * Content Type is JSON by default.
     */
    protected Response status(HttpStatus httpStatus)
    {
        Response response = new Response();
        response.setStatus(httpStatus);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        if (httpStatus.getCode() >= 400)
        {
            response.setBody("{ \"error\": \""+ httpStatus.getMessage() + "\"}");
        }
        else
        {
            response.setBody("{ \"response\": \"" + httpStatus.getMessage() + "\"}");
        }

        return response;
    }

    /**
     * Return a Response using a httpStatus Code.
     * You can specify the body of the status code the user receives.
     * Content Type is JSON by default.
     */
    protected Response statusJsonBody(HttpStatus httpStatus, String body)
    {
        Response response = new Response();
        response.setStatus(httpStatus);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        if (httpStatus.getCode() >= 400)
        {
            response.setBody("{ \"error\": \""+ body + "\"}");
        }
        else
        {
            response.setBody(body);
        }

        return response;
    }
}
