package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.server.http.*;

public class CardController extends Controller
{
    private final CardService cardService = new CardService(new CardRepository(), new UserRepository());

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
        return cardService.getAllCards(request);
    }
}
