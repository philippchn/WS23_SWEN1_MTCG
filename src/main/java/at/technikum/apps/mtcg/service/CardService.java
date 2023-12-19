package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.repository.CardRepository;

import java.sql.SQLException;

public class CardService
{
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository)
    {
        this.cardRepository = cardRepository;
    }

    public void deleteAll() throws SQLException
    {
        cardRepository.deleteAll();
    }
}
