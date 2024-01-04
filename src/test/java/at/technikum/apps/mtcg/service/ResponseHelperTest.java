package at.technikum.apps.mtcg.service;

import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseHelperTest
{

    @Test
    void status()
    {
        //given

        //when
        Response response = ResponseHelper.status(HttpStatus.OK);

        //then
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void statusJsonBody()
    {
        //given

        //when
        Response response = ResponseHelper.statusJsonBody(HttpStatus.OK, "[]");

        //then
        assertEquals(200, response.getStatusCode());
        assertEquals("[]", response.getBody());
    }
}