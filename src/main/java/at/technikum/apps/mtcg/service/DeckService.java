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
import java.util.Optional;

public class DeckService
{
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final AuthorizationTokenHelper authorizationTokenHelper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public DeckService(CardRepository cardRepository, UserRepository userRepository, AuthorizationTokenHelper authorizationTokenHelper)
    {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.authorizationTokenHelper = authorizationTokenHelper;
    }

    public Response configureDeck(Request request)
    {
        String token = request.getAuthorizationToken();
        String usernameFromToken = authorizationTokenHelper.getUsernameFromToken(request);

        if (token.equals("INVALID") || userRepository.findUserByUsername(usernameFromToken).isEmpty())
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        String[] deck;
        try
        {
            deck = objectMapper.readValue(request.getBody(), String[].class);
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.BAD_REQUEST);
        }

        if (deck.length != 4)
        {
            return ResponseHelper.status(HttpStatus.BAD_REQUEST);
        }

        for (String s : deck)
        {
            Optional<String> cardOwner = cardRepository.getCardOwner(s);
            if (cardOwner.isEmpty())
            {
                return ResponseHelper.status(HttpStatus.FORBIDDEN);
            }
            if (!cardOwner.get().equals(usernameFromToken))
            {
                return ResponseHelper.status(HttpStatus.FORBIDDEN);
            }
        }

        try
        {
            cardRepository.createDeck(usernameFromToken, deck);
        }
        catch (SQLException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseHelper.status(HttpStatus.OK);
    }

    public Response showDeck(Request request)
    {
        String token = request.getAuthorizationToken();
        String usernameFromToken = authorizationTokenHelper.getUsernameFromToken(request);

        if (token.equals("INVALID") || userRepository.findUserByUsername(usernameFromToken).isEmpty())
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        List<RequestCard> deck = cardRepository.getSimpleDeck(usernameFromToken);
        if (deck.isEmpty())
        {
            return ResponseHelper.status(HttpStatus.NO_CONTENT);
        }

        if (request.getRoute().endsWith("format=plain"))
        {
            return ResponseHelper.statusJsonBody(HttpStatus.OK, deck.toString());
        }

        String deckJson;
        try
        {
            deckJson = objectMapper.writeValueAsString(deck);
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseHelper.statusJsonBody(HttpStatus.OK, deckJson);
    }
}
