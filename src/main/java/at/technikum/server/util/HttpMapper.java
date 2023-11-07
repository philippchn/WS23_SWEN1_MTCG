package at.technikum.server.util;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class HttpMapper {
    Request toRequestObject(String request)
    {
        return new Request();
    }

    String toRequestString(Request request)
    {
        return "";
    }

    Response toResponseObject(String response)
    {
        return new Response();
    }

    String toResponseString(Response response)
    {
        return "";
    }
}
