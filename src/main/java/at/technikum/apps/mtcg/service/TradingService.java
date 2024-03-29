package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Trade;
import at.technikum.apps.mtcg.entity.card.DBCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.TradingRepository;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TradingService
{
    private final TradingRepository tradingRepository;
    private final CardRepository cardRepository;
    private final AuthorizationTokenHelper authorizationTokenHelper;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public TradingService(TradingRepository tradingRepository, CardRepository cardRepository, AuthorizationTokenHelper authorizationTokenHelper)
    {
        this.tradingRepository = tradingRepository;
        this.cardRepository = cardRepository;
        this.authorizationTokenHelper = authorizationTokenHelper;
    }

    public Response getAvailableTrades()
    {
        List<Trade> trades = tradingRepository.getAvailableTrades();

        if (trades.isEmpty())
        {
            return ResponseHelper.status(HttpStatus.NO_CONTENT);
        }

        String tradesJson;
        try
        {
            tradesJson = objectMapper.writeValueAsString(trades);
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseHelper.statusJsonBody(HttpStatus.OK, tradesJson);
    }

    public Response createTrade(Request request)
    {
        if (authorizationTokenHelper.invalidToken(request))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        Trade trade;
        try
        {
            trade = objectMapper.readValue(request.getBody(), Trade.class);
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.BAD_REQUEST);
        }


        if (cardRepository.isCardInDeck(trade.CardToTrade()))
        {
            return ResponseHelper.status(HttpStatus.FORBIDDEN);
        }


        Optional<String> username = cardRepository.getCardOwner(trade.CardToTrade());
        if (username.isEmpty() || !username.get().equals(authorizationTokenHelper.getUsernameFromToken(request)))
        {
            return ResponseHelper.status(HttpStatus.FORBIDDEN);
        }

        try
        {
            tradingRepository.createTrade(trade, authorizationTokenHelper.getUsernameFromToken(request));
        }
        catch (SQLException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseHelper.status(HttpStatus.CREATED);
    }

    public Response deleteTrade(Request request)
    {
        String tradingDealId = request.getRoute().replace("/tradings/", "");

        if (authorizationTokenHelper.invalidToken(request))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }


        Optional<String> dealOwner = tradingRepository.getTradeOwner(tradingDealId);
        if (dealOwner.isEmpty())
        {
            return ResponseHelper.status(HttpStatus.NOT_FOUND);
        }
        if (!authorizationTokenHelper.getUsernameFromToken(request).equals(dealOwner.get()))
        {
            return ResponseHelper.status(HttpStatus.FORBIDDEN);
        }

        try
        {
            tradingRepository.deleteTrade(tradingDealId);
        }
        catch (SQLException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseHelper.status(HttpStatus.OK);
    }

    public Response doTrade(Request request)
    {
        if (authorizationTokenHelper.invalidToken(request))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        String tradingDealId = request.getRoute().replace("/tradings/", "");
        String cardId;
        try
        {
            cardId = objectMapper.readValue(request.getBody(), String.class);
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.BAD_REQUEST);
        }

        Optional<String> cardOwner = cardRepository.getCardOwner(cardId);
        if (cardOwner.isEmpty() || !cardOwner.get().equals(authorizationTokenHelper.getUsernameFromToken(request)))
        {
            return ResponseHelper.status(HttpStatus.FORBIDDEN);
        }

        Optional<String> dealOwner = tradingRepository.getTradeOwner(tradingDealId);
        if (dealOwner.isEmpty())
        {
            return ResponseHelper.status(HttpStatus.NOT_FOUND);
        }
        if (dealOwner.get().equals(authorizationTokenHelper.getUsernameFromToken(request)))
        {
            return ResponseHelper.status(HttpStatus.FORBIDDEN);
        }


        Optional<Float> tradeMindmg = tradingRepository.getMindmg(tradingDealId);
        Optional<DBCard> card = cardRepository.getCard(cardId);
        if (card.isEmpty() || tradeMindmg.isEmpty() || card.get().damage() < tradeMindmg.get())
        {
            return ResponseHelper.status(HttpStatus.FORBIDDEN);
        }

        try
        {
            cardRepository.updateCardOwner(cardId, dealOwner.get());
            cardRepository.updateCardOwner(tradingRepository.getCardIdFromTrade(tradingDealId).get(), authorizationTokenHelper.getUsernameFromToken(request));
            tradingRepository.deleteTrade(tradingDealId);
        }
        catch (SQLException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseHelper.status(HttpStatus.OK);
    }
}
