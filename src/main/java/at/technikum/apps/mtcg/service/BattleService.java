package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.DBCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class BattleService
{
    private final CardRepository cardRepository;
    private List<DBCard> playerOneDeck;
    private  List<DBCard> playerTwoDeck;
    public BattleService(CardRepository cardRepository)
    {
        this.cardRepository = cardRepository;
    }

    public boolean invalidUser(Request request)
    {
        return AuthorizationTokenHelper.invalidToken(request);
    }

    public Response startBattle(Request request, Request enemyPendingRequest)
    {
        System.out.println("Battle started");

        String playerOneUsername = AuthorizationTokenHelper.getUsernameFromToken(request);
        String playerTwoUsername = AuthorizationTokenHelper.getUsernameFromToken(enemyPendingRequest);

        try
        {
            playerOneDeck = cardRepository.getDetailDeck(playerOneUsername);
            playerTwoDeck = cardRepository.getDetailDeck(playerTwoUsername);
        }
        catch (SQLException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        int maxGames = 0;
        while(!playerOneDeck.isEmpty() && !playerTwoDeck.isEmpty() && maxGames++ < 100)
        {
            cardFight();
        }

        if (playerOneDeck.isEmpty())
        {
            return ResponseHelper.statusJsonBody(HttpStatus.OK,"{\n\t\"winner\": \"" + playerTwoUsername + "\"\n}");
        }
        else if(playerTwoDeck.isEmpty())
        {
            return ResponseHelper.statusJsonBody(HttpStatus.OK,"{\n\t\"winner\": \"" + playerOneUsername + "\"\n}");
        }
        else
        {
            return ResponseHelper.statusJsonBody(HttpStatus.OK,"{\n\t\"winner\": \"No winner\"\n}");
        }
    }

    private void cardFight()
    {
        int randomIndexOne = new Random().nextInt(playerOneDeck.size());
        int randomIndexTwo = new Random().nextInt(playerTwoDeck.size());

        float playerOneDamage = damageModifier(playerOneDeck.get(randomIndexOne), playerTwoDeck.get(randomIndexTwo));
        float playerTwoDamage = damageModifier(playerTwoDeck.get(randomIndexTwo), playerOneDeck.get(randomIndexOne));

        if (playerOneDamage > playerTwoDamage)
        {
            playerOneDeck.add(playerTwoDeck.get(randomIndexTwo));
            playerTwoDeck.remove(randomIndexTwo);
        }
        else if (playerOneDamage < playerTwoDamage)
        {
            playerTwoDeck.add(playerOneDeck.get(randomIndexOne));
            playerOneDeck.remove(randomIndexOne);
        }
    }

    private float damageModifier(DBCard attacker, DBCard defender)
    {
        float damage = attacker.damage();

        if (!attacker.isMonster())
        {
            return damageSpellModifier(attacker, defender, damage);
        }

        return damageMonsterModifier(attacker, defender, damage);
    }

    private float damageMonsterModifier(DBCard attacker, DBCard defender, float damage) {
        // Goblin -> Dragon
        if (attacker.name().contains("Goblin") && defender.name().equals("Dragon"))
        {
            return 0;
        }
        // Ork -> Wizard
        if (attacker.name().equals("Ork") && !defender.isMonster() )
        {
            return 0;
        }
//        //WaterSpell -> Knight
//        if (attacker.name().equals("WaterSpell") && defender.name().equals("Knight"))
//        {
//            return 10000;
//        }
        // Dragon -> FireElf
        if (attacker.name().equals("Dragon") && defender.name().equals("FireElf"))
        {
            return 0;
        }
        return damage;
    }

    private float damageSpellModifier(DBCard attacker, DBCard defender, float damage)
    {
        // Spell -> Kraken
        if (defender.name().equals("Kraken"))
        {
            return 0;
        }

        // Water -> Fire
        if (attacker.elementType().equals("water") && defender.elementType().equals("fire"))
        {
            damage = damage * 2;
        }
        // Fire -> Water
        if (attacker.elementType().equals("fire") && defender.elementType().equals("water"))
        {
            damage = damage / 2;
        }

        // Fire -> Normal
        if (attacker.elementType().equals("fire") && defender.elementType().equals("regular"))
        {
            damage = damage * 2;
        }
        // Normal -> Fire
        if (attacker.elementType().equals("regular") && defender.elementType().equals("fire"))
        {
            damage = damage / 2;
        }

        // Normal -> Water
        if (attacker.elementType().equals("regular") && defender.elementType().equals("water"))
        {
            damage = damage * 2;
        }
        // Water -> Normal
        if (attacker.elementType().equals("water") && defender.elementType().equals("regular"))
        {
            damage = damage / 2;
        }

        return damage;
    }
}
