package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.CardType;
import at.technikum.apps.mtcg.entity.card.DBCard;
import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;

public class PackageService
{
    private final PackageRepository packageRepository;
    private final CardRepository cardRepository;
    private final AuthorizationTokenHelper authorizationTokenHelper;

    private final ObjectMapper objectMapper = new ObjectMapper();



    public PackageService(PackageRepository packageRepository, CardRepository cardRepository, AuthorizationTokenHelper authorizationTokenHelper)
    {
        this.packageRepository = packageRepository;
        this.cardRepository = cardRepository;
        this.authorizationTokenHelper = authorizationTokenHelper;
    }

    public Response createPackage(Request request)
    {
        if (request.getAuthorizationToken().equals("INVALID"))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }
        if (!authorizationTokenHelper.isAdmin(request))
        {
            return ResponseHelper.status(HttpStatus.FORBIDDEN);
        }


        RequestCard[] requestCards;
        try
        {
            requestCards = objectMapper.readValue(request.getBody(), RequestCard[].class);
            if (requestCards.length != 5)
            {
                return ResponseHelper.status(HttpStatus.BAD_REQUEST);
            }
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.BAD_REQUEST);
        }

        try
        {
            for (RequestCard requestCard : requestCards)
            {
                cardRepository.saveCard(cardConverter(requestCard));
            }
            packageRepository.savePackage(requestCards);
        }
        catch (SQLException e)
        {
            if (e.getSQLState().equals("23505"))
            {
                return ResponseHelper.status(HttpStatus.CONFLICT);
            }
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (IllegalArgumentException e)
        {
            return ResponseHelper.status(HttpStatus.BAD_REQUEST);
        }

        return ResponseHelper.status(HttpStatus.CREATED);
    }

    private DBCard cardConverter(RequestCard requestCard)
    {
        CardType cardType = CardType.createType(requestCard.Name());
        return new DBCard(requestCard.Id(), requestCard.Name(), requestCard.Damage(), cardType.isMonster(), cardType.getElementType().toString());
    }
}
