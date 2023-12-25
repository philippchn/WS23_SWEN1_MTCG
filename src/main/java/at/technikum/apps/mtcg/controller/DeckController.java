package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DeckController extends Controller{

    private final CardService cardService = new CardService(new CardRepository());
    private final UserService userService = new UserService(new UserRepository());


    private final ObjectMapper objectMapper = new ObjectMapper();

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
        String token = request.getAuthorizationToken();
        String usernameFromToken = AuthorizationTokenHelper.getUsernameFromToken(request);
        try
        {
            if (token.equals("INVALID") || userService.findUserByUsername(usernameFromToken).isEmpty())
            {
                return status(HttpStatus.UNAUTHORIZED);
            }
        }
        catch (SQLException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String[] deck;
        try
        {
            deck = objectMapper.readValue(request.getBody(), String[].class);
        }
        catch (JsonProcessingException e)
        {
            return status(HttpStatus.BAD_REQUEST);
        }

        if (deck.length != 4)
        {
            return status(HttpStatus.BAD_REQUEST);
        }

        try
        {
            for (String s : deck)
            {
                Optional<String> cardOwner = cardService.getCardOwner(s);
                if (cardOwner.isEmpty())
                {
                    return status(HttpStatus.FORBIDDEN);
                }
                if (!cardOwner.get().equals(usernameFromToken))
                {
                    return status(HttpStatus.FORBIDDEN);
                }
            }
        }
        catch (SQLException e)
        {
            return status(HttpStatus.BAD_REQUEST);
        }

        try
        {
            cardService.createDeck(usernameFromToken, deck);
        }
        catch (SQLException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return status(HttpStatus.OK);
    }

    private Response showDeck(Request request)
    {
        String token = request.getAuthorizationToken();
        String usernameFromToken = AuthorizationTokenHelper.getUsernameFromToken(request);
        try
        {
            if (token.equals("INVALID") || userService.findUserByUsername(usernameFromToken).isEmpty())
            {
                return status(HttpStatus.UNAUTHORIZED);
            }
        }
        catch (SQLException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<RequestCard> deck;
        try
        {
            deck = cardService.getDeck(usernameFromToken);
        }
        catch (SQLException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (request.getRoute().endsWith("format=plain"))
        {
            return statusJsonBody(HttpStatus.OK, deck.toString());
        }

        String deckJson;
        try
        {
            deckJson = objectMapper.writeValueAsString(deck);
        }
        catch (JsonProcessingException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return statusJsonBody(HttpStatus.OK, deckJson);
    }
}
