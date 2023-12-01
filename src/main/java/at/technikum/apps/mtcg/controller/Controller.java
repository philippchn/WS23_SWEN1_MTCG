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
     */
    protected Response status(HttpStatus httpStatus) {
        Response response = new Response();
        response.setStatus(httpStatus);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody("{ \"error\": \""+ httpStatus.getMessage() + "\"}");

        return response;
    }

    /**
     * Return a Response using a httpStatus Code.
     * You can specify the error message of the status code the user receives
     */
    protected Response status(HttpStatus httpStatus, String errorMessage) {
        Response response = new Response();
        response.setStatus(httpStatus);
        response.setContentType(HttpContentType.APPLICATION_JSON);
        response.setBody("{ \"error\": \"" + errorMessage + "\"}");

        return response;
    }
}
