package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;

public class CardService
{
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public CardService(CardRepository cardRepository, UserRepository userRepository)
    {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public Response getAllCards(Request request)
    {
        String token = request.getAuthorizationToken();
        String usernameFromToken = AuthorizationTokenHelper.getUsernameFromToken(request);
        try
        {
            if (token.equals("INVALID") || userRepository.findUserByUsername(usernameFromToken).isEmpty())
            {
                return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
            }
        }
        catch (SQLException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<RequestCard> allCards;
        try
        {
            allCards = cardRepository.getAllCardsOfUser(usernameFromToken);
        }
        catch (SQLException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (allCards.isEmpty())
        {
            return ResponseHelper.status(HttpStatus.NO_CONTENT);
        }

        String allCardsJson;
        try
        {
            allCardsJson = objectMapper.writeValueAsString(allCards);
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseHelper.statusJsonBody(HttpStatus.OK, allCardsJson);
    }
}
