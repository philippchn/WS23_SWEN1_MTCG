package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.AuthorizationTokenHelper;
import at.technikum.apps.mtcg.service.DeckService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class DeckController extends Controller{
    private final DeckService deckService = new DeckService(new CardRepository(), new UserRepository(), new AuthorizationTokenHelper());
    @Override
    public boolean supports(String route) {
        return route.startsWith("/deck");
    }

    @Override
    public Response handle(Request request) {
        return switch (request.getMethod()) {
            case "PUT" -> configureDeck(request);
            case "GET" -> showDeck(request);
            default ->
                    status(HttpStatus.METHOD_NOT_ALLOWED);
        };
    }

    private Response configureDeck(Request request)
    {
        return deckService.configureDeck(request);
    }

    private Response showDeck(Request request)
    {
        return deckService.showDeck(request);
    }
}
