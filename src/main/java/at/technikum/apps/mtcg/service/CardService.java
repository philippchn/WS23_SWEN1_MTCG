package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CardService
{
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository)
    {
        this.cardRepository = cardRepository;
    }

    public List<RequestCard> getAllUserCards(String username) throws SQLException
    {
        return cardRepository.getAllCardsOfUser(username);
    }

    public Optional<String> getCardOwner(String cardId) throws SQLException
    {
        return cardRepository.getCardOwner(cardId);
    }

    public void createDeck(String username, String[] ids) throws SQLException
    {
        cardRepository.createDeck(username, ids);
    }

    public List<RequestCard> getDeck(String username) throws SQLException
    {
        return cardRepository.getDeck(username);
    }

    public void deleteAll() throws SQLException
    {
        cardRepository.deleteAll();
    }
}
