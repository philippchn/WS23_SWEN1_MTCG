package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;

public class TransactionController extends Controller
{
    private final UserService userService = new UserService(new UserRepository());
    private final PackageService packageService = new PackageService(new PackageRepository(), new CardRepository(), new UserRepository());

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public boolean supports(String route)
    {
        return route.startsWith("/transactions");
    }

    @Override
    public Response handle(Request request)
    {
        if (request.getMethod().equals("POST"))
        {
            return buyPackage(request);
        }
        else
        {
            return status(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private Response buyPackage(Request request)
    {
        if (request.getAuthorizationToken().equals("INVALID"))
        {
            return status(HttpStatus.UNAUTHORIZED);
        }

        String username = AuthorizationTokenHelper.getUsernameFromToken(request);
        int coins;
        List<RequestCard> acquiredCards;
        String cardsJson;

        try
        {
            if (AuthorizationTokenHelper.invalidToken(request))
            {
                return status(HttpStatus.UNAUTHORIZED);
            }

            coins = userService.getCoins(username);
            if (coins < 5)
            {
                return status(HttpStatus.FORBIDDEN);
            }

            acquiredCards = packageService.buyPackage(username);
            if(acquiredCards.isEmpty())
            {
                return status(HttpStatus.NOT_FOUND);
            }
            userService.takeFiveCoins(username);

            cardsJson = cardsToJson(acquiredCards);
        }
        catch (SQLException | JsonProcessingException e)
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return statusJsonBody(HttpStatus.OK, cardsJson);
    }

    private String cardsToJson(List<RequestCard> cards) throws JsonProcessingException
    {
        StringBuilder cardsJson = new StringBuilder().append("[\n");
        for (RequestCard s : cards)
        {
            cardsJson.append("\t").append(objectMapper.writeValueAsString(s)).append(",\n");
        }
        cardsJson.append("]");
        return cardsJson.toString();
    }
}
