package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.NewCardService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;

public class CardController extends Controller
{
    private final NewCardService newCardService = new NewCardService(new CardRepository(), new UserRepository());

    @Override
    public boolean supports(String route)
    {
        return route.startsWith("/cards");
    }

    @Override
    public Response handle(Request request)
    {
        if (!request.getMethod().equals("GET"))
        {
            return status(HttpStatus.METHOD_NOT_ALLOWED);
        }
        return getAllCards(request);
    }

    public Response getAllCards(Request request)
    {
        return newCardService.getAllCards(request);
    }
}
