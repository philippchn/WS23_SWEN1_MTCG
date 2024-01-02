package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class TransactionService
{
    private final PackageRepository packageRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public TransactionService(PackageRepository packageRepository, UserRepository userRepository)
    {
        this.packageRepository = packageRepository;
        this.userRepository = userRepository;
    }

    public Response buyPackage(Request request)
    {
        if (request.getAuthorizationToken().equals("INVALID"))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        String username = AuthorizationTokenHelper.getUsernameFromToken(request);
        int coins;
        List<RequestCard> acquiredCards;
        String cardsJson;

        try
        {
            if (AuthorizationTokenHelper.invalidToken(request))
            {
                return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
            }

            coins = userRepository.getCoins(username);
            if (coins < 5)
            {
                return ResponseHelper.status(HttpStatus.FORBIDDEN);
            }

            acquiredCards = buyPackage(username);

            if(acquiredCards.isEmpty())
            {
                return ResponseHelper.status(HttpStatus.NOT_FOUND);
            }
            userRepository.takeFiveCoins(username);

            cardsJson = cardsToJson(acquiredCards);
        }
        catch (SQLException | JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseHelper.statusJsonBody(HttpStatus.OK, cardsJson);
    }

    private List<RequestCard> buyPackage(String username) throws SQLException
    {
        List<Integer> ids = packageRepository.getAllAvailablePackageId();
        if (ids.isEmpty())
        {
            return Collections.emptyList();
        }
        packageRepository.buyPackage(username, ids.get(0));
        return packageRepository.getCardsFromPackage(ids.get(0));
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
