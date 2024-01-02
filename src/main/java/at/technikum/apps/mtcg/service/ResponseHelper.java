package at.technikum.apps.mtcg.service;

import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Response;

public class ResponseHelper
{
    static protected Response status(HttpStatus httpStatus)
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
    static protected Response statusJsonBody(HttpStatus httpStatus, String body)
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
