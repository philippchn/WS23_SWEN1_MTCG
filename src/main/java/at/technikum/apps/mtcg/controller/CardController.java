package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.AuthorizationTokenHelper;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.server.http.*;

public class CardController extends Controller
{
    private final CardService cardService = new CardService(new CardRepository(), new UserRepository(), new PackageRepository(), new AuthorizationTokenHelper());

    @Override
    public boolean supports(String route)
    {
        return route.startsWith("/cards");
    }

    @Override
    public Response handle(Request request)
    {
        return switch (request.getMethod()) {
            case "POST" -> upgrade(request);
            case "GET" -> getAllCards(request);
            default ->
                    status(HttpStatus.METHOD_NOT_ALLOWED);
        };
    }

    public Response getAllCards(Request request)
    {
        return cardService.getAllCards(request);
    }

    private Response upgrade(Request request)
    {
        return cardService.upgradeCard(request);
    }
}
