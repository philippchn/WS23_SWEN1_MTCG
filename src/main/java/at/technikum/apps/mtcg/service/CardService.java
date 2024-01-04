package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.DBCard;
import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.plugin.AuthenticationRequestType;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CardService
{
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final PackageRepository packageRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public CardService(CardRepository cardRepository, UserRepository userRepository, PackageRepository packageRepository)
    {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.packageRepository = packageRepository;
    }

    public Response getAllCards(Request request)
    {
        String token = request.getAuthorizationToken();
        String usernameFromToken = AuthorizationTokenHelper.getUsernameFromToken(request);

        if (token.equals("INVALID") || userRepository.findUserByUsername(usernameFromToken).isEmpty())
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        List<RequestCard> allCards = cardRepository.getAllCardsOfUser(usernameFromToken);
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

    public Response upgradeCard(Request request)
    {
        String[] cards;
        try
        {
            cards = objectMapper.readValue(request.getBody(), String[].class);
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.BAD_REQUEST);
        }
        if (cards.length != 2)
        {
            return ResponseHelper.status(HttpStatus.BAD_REQUEST);
        }

        if(cardRepository.isCardInDeck(cards[0]) || cardRepository.isCardInDeck(cards[1]))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        Optional<String> cardOneOwner = cardRepository.getCardOwner(cards[0]);
        Optional<String> cardTwoOwner = cardRepository.getCardOwner(cards[1]);
        if (cardOneOwner.isEmpty() || cardTwoOwner.isEmpty())
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }
        String username = AuthorizationTokenHelper.getUsernameFromToken(request);
        if (!cardOneOwner.get().equals(username) || !cardTwoOwner.get().equals(username))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        Optional<DBCard> card1;
        Optional<DBCard> card2;

        card1 = cardRepository.getCard(cards[0]);
        card2 = cardRepository.getCard(cards[1]);
        if (card1.isEmpty() || card2.isEmpty())
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }
        if (!card1.get().name().equals(card2.get().name()))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        try
        {
            packageRepository.removeCardFromPackage(card2.get().id());
            cardRepository.deleteCard(card2.get().id());
            float newDamage = card1.get().damage() + card2.get().damage() + 20;
            cardRepository.setCardDamage(card1.get().id(), newDamage);
        }
        catch (SQLException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseHelper.status(HttpStatus.OK);
    }
}
