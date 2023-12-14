package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;

public class PackageController extends Controller
{

    private final PackageService packageService = new PackageService(new PackageRepository(), new CardRepository());

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(String route)
    {
        return route.startsWith("/packages");
    }

    @Override
    public Response handle(Request request)
    {
        if (request.getMethod().equals("POST"))
        {
            return createPackage(request);
        }
        else
        {
            return status(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private Response createPackage(Request request)
    {
        RequestCard[] requestCards;
        try
        {
            requestCards = objectMapper.readValue(request.getBody(), RequestCard[].class);
            if (requestCards.length != 5)
            {
                return statusCustomBody(HttpStatus.BAD_REQUEST, "JSON invalid");
            }
        }
        catch (JsonProcessingException e)
        {
            return statusCustomBody(HttpStatus.BAD_REQUEST, "JSON invalid");
        }

        try
        {
            packageService.savePackage(requestCards);
        }
        catch (SQLException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (IllegalArgumentException e)
        {
            return statusCustomBody(HttpStatus.BAD_REQUEST, "A name is invalid");
        }

        return status(HttpStatus.OK);
    }
}
