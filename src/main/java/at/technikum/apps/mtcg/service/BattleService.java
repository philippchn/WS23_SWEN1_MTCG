package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.DBCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BattleService
{
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final AuthorizationTokenHelper authorizationTokenHelper;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<DBCard> playerOneDeck;
    private  List<DBCard> playerTwoDeck;
    private List<String> battleLog = new ArrayList<>();
    public final String ERROR = "ERROR";
    public BattleService(CardRepository cardRepository, UserRepository userRepository, AuthorizationTokenHelper authorizationTokenHelper)
    {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.authorizationTokenHelper = authorizationTokenHelper;
    }

    public boolean invalidUser(Request request)
    {
        return authorizationTokenHelper.invalidToken(request);
    }


    public String startBattle(Request request, Request enemyPendingRequest)
    {
        battleLog = new ArrayList<>();
        String playerOneUsername = authorizationTokenHelper.getUsernameFromToken(request);
        String playerTwoUsername = authorizationTokenHelper.getUsernameFromToken(enemyPendingRequest);


        playerOneDeck = cardRepository.getDetailDeck(playerOneUsername);
        playerTwoDeck = cardRepository.getDetailDeck(playerTwoUsername);

        if (playerOneDeck.isEmpty() || playerTwoDeck.isEmpty())
        {
            return ERROR;
        }

        battleLog.add("STARTING BATTLE!");
        battleLog.add(playerOneUsername + " VS " + playerTwoUsername);
        battleLog.add("||||||||||||||||||||||||");

        int maxGames = 0;
        while(!playerOneDeck.isEmpty() && !playerTwoDeck.isEmpty() && maxGames++ < 100)
        {
            cardFight(playerOneUsername, playerTwoUsername);
        }

        battleLog.add("||||||||||||||||||||||||");

        checkWinner(playerTwoUsername, playerOneUsername);

        battleLog.add("BATTLE OVER!");

        String battleLogJson;
        try
        {
            battleLogJson = objectMapper.writeValueAsString(battleLog);
        }
        catch (JsonProcessingException e)
        {
            return ERROR;
        }

        return battleLogJson;
    }

    private void cardFight(String playerOne, String playerTwo)
    {
        int randomIndexOne = new Random().nextInt(playerOneDeck.size());
        int randomIndexTwo = new Random().nextInt(playerTwoDeck.size());

        float playerOneDamage = damageModifier(playerOneDeck.get(randomIndexOne), playerTwoDeck.get(randomIndexTwo));
        float playerTwoDamage = damageModifier(playerTwoDeck.get(randomIndexTwo), playerOneDeck.get(randomIndexOne));

        if (playerOneDamage > playerTwoDamage)
        {
            battleLog.add("-) " + playerOneDeck.get(randomIndexOne).name() + " card of " +  playerOne + " won with " + playerOneDamage + " damage dealt against a " + playerTwoDeck.get(randomIndexTwo).name() + "!");
            playerOneDeck.add(playerTwoDeck.get(randomIndexTwo));
            playerTwoDeck.remove(randomIndexTwo);
        }
        else if (playerOneDamage < playerTwoDamage)
        {
            battleLog.add("-) " + playerTwoDeck.get(randomIndexTwo).name() + " card of " +  playerTwo + " won with " + playerTwoDamage + " damage dealt against a " + playerOneDeck.get(randomIndexOne).name() + "!");
            playerTwoDeck.add(playerOneDeck.get(randomIndexOne));
            playerOneDeck.remove(randomIndexOne);
        }
        else
        {
            battleLog.add("-) None of the cards won!");
        }
    }

    float damageModifier(DBCard attacker, DBCard defender)
    {
        float damage = attacker.damage();

        if (!attacker.isMonster())
        {
            return damageSpellModifier(attacker, defender, damage);
        }

        return damageMonsterModifier(attacker, defender, damage);
    }

    private float damageMonsterModifier(DBCard attacker, DBCard defender, float damage)
    {
        // Goblin -> Dragon
        if (attacker.name().contains("Goblin") && defender.name().equals("Dragon"))
        {
            battleLog.add("The Goblin is too scared too attack the Dragon!");
            return 0;
        }
        // Ork -> Wizard
//        if (attacker.name().equals("Ork") && !defender.isMonster() )
//        {
//            battleLog.add("The Wizzard controls the Ork! It can't attack!");
//            return 0;
//        }
        // Dragon -> FireElf
        if (attacker.name().equals("Dragon") && defender.name().equals("FireElf"))
        {
            battleLog.add("The Fire Elf evades the attacks of the Dragon!");
            return 0;
        }
        return damage;
    }

    private float damageSpellModifier(DBCard attacker, DBCard defender, float damage)
    {
        //WaterSpell -> Knight
        if (attacker.name().equals("WaterSpell") && defender.name().equals("Knight"))
        {
            battleLog.add("The WaterSpell drowns the Knight!");
            return 10000;
        }

        // Spell -> Kraken
        if (defender.name().equals("Kraken"))
        {
            battleLog.add("The Kraken is immune to Spells!");
            return 0;
        }

        // Water -> Fire
        if (attacker.elementType().equals("water") && defender.elementType().equals("fire"))
        {
            battleLog.add("Water is very effective against Fire!");
            return damage * 2;
        }
        // Fire -> Water
        if (attacker.elementType().equals("fire") && defender.elementType().equals("water"))
        {
            return damage / 2;
        }

        // Fire -> Normal
        if (attacker.elementType().equals("fire") && defender.elementType().equals("regular"))
        {
            battleLog.add("Fire is very effective against Normal!");
            return damage * 2;
        }
        // Normal -> Fire
        if (attacker.elementType().equals("regular") && defender.elementType().equals("fire"))
        {
            return damage / 2;
        }

        // Normal -> Water
        if (attacker.elementType().equals("regular") && defender.elementType().equals("water"))
        {
            battleLog.add("Normal is very effective against Water!");
            return damage * 2;
        }
        // Water -> Normal
        if (attacker.elementType().equals("water") && defender.elementType().equals("regular"))
        {
            return damage / 2;
        }

        return damage;
    }

    void checkWinner(String playerTwoUsername, String playerOneUsername)
    {
        if (playerOneDeck.isEmpty())
        {
            battleLog.add(playerTwoUsername + " WON!");
            setElo(playerTwoUsername, playerOneUsername);
        }
        else if (playerTwoDeck.isEmpty())
        {
            battleLog.add(playerOneUsername + " WON!");
            setElo(playerOneUsername, playerTwoUsername);
        }
        else
        {
            battleLog.add("BATTLE ENDS IN A DRAW!");
        }
    }

    private void setElo(String winnerUsername, String loserUsername)
    {
        try
        {
            userRepository.giveThreeElo(winnerUsername);
            userRepository.takeFiveElo(loserUsername);
        }
        catch (SQLException e)
        {
            System.out.println("ERROR SETTING ELO");
        }
    }
}
