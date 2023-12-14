package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.entity.card.CardType;
import at.technikum.apps.mtcg.entity.card.DBCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;

import java.sql.SQLException;

public class PackageService
{
    private final PackageRepository packageRepository;
    private final CardRepository cardRepository;

    public PackageService(PackageRepository packageRepository, CardRepository cardRepository)
    {
        this.packageRepository = packageRepository;
        this.cardRepository = cardRepository;
    }

    public void savePackage(RequestCard[] requestCards) throws SQLException
    {
        for (RequestCard requestCard : requestCards)
        {
            cardRepository.saveCard(cardConverter(requestCard));
        }
        packageRepository.savePackage(requestCards);
    }

    private DBCard cardConverter(RequestCard requestCard)
    {
        CardType cardType = CardType.createType(requestCard.Name());
        return new DBCard(requestCard.Id(), requestCard.Name(), requestCard.Damage(), cardType.isMonster(), cardType.getElementType().toString());
    }
}
