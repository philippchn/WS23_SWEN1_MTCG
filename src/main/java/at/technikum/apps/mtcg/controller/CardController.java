package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;

public class CardController extends Controller
{
    private final CardService cardService = new CardService(new CardRepository());
    private final UserService userService = new UserService(new UserRepository());

    private final ObjectMapper objectMapper = new ObjectMapper();


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

        List<RequestCard> allCards;
        try
        {
            allCards = cardService.getAllUserCards(usernameFromToken);
        }
        catch (SQLException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (allCards.isEmpty())
        {
            return status(HttpStatus.NO_CONTENT);
        }

        String allCardsJson;
        try
        {
            allCardsJson = objectMapper.writeValueAsString(allCards);
        }
        catch (JsonProcessingException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return statusJsonBody(HttpStatus.OK, allCardsJson);
    }
}
