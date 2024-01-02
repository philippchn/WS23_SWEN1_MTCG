package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.RequestCard;
import at.technikum.apps.mtcg.entity.card.CardType;
import at.technikum.apps.mtcg.entity.card.DBCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.repository.UserRepository;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Deprecated
public class PackageService
{
    private final PackageRepository packageRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public PackageService(PackageRepository packageRepository, CardRepository cardRepository, UserRepository userRepository)
    {
        this.packageRepository = packageRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
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

    public List<RequestCard> buyPackage(String username) throws SQLException
    {
        List<Integer> ids = packageRepository.getAllAvailablePackageId();
        if (ids.isEmpty())
        {
            return Collections.emptyList();
        }
        packageRepository.buyPackage(username, ids.get(0));
        return packageRepository.getCardsFromPackage(ids.get(0));
    }

    public void deleteAll() throws SQLException
    {
        packageRepository.deleteAll();
    }
}
