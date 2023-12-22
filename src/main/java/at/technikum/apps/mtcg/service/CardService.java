package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.repository.CardRepository;

import java.sql.SQLException;
import java.util.List;

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

    public void deleteAll() throws SQLException
    {
        cardRepository.deleteAll();
    }
}
