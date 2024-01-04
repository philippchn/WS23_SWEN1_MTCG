package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.card.DBCard;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BattleServiceTest
{

    @Test
    void damageModifierGoblinAttacksDragon()
    {
        //given
        CardRepository cardRepositoryMock = mock(CardRepository.class);
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);

        BattleService battleService = new BattleService(cardRepositoryMock, userRepositoryMock, authorizationTokenHelperMock);

        DBCard attacker = new DBCard("id1", "WaterGoblin", 10, true, "water");
        DBCard defender = new DBCard("id2", "Dragon", 10, true, "regular");

        //when
        float dmdg = battleService.damageModifier(attacker, defender);

        //then
        assertEquals(0, dmdg);
    }

    @Test
    void damageModifierDragonAttacksFireElf()
    {
        //given
        CardRepository cardRepositoryMock = mock(CardRepository.class);
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);

        BattleService battleService = new BattleService(cardRepositoryMock, userRepositoryMock, authorizationTokenHelperMock);

        DBCard attacker = new DBCard("id1", "Dragon", 10, true, "regular");
        DBCard defender = new DBCard("id2", "FireElf", 10, true, "fire");

        //when
        float dmdg = battleService.damageModifier(attacker, defender);

        //then
        assertEquals(0, dmdg);
    }

    @Test
    void damageModifierWaterspellAttacksKnight()
    {
        //given
        CardRepository cardRepositoryMock = mock(CardRepository.class);
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);

        BattleService battleService = new BattleService(cardRepositoryMock, userRepositoryMock, authorizationTokenHelperMock);

        DBCard attacker = new DBCard("id1", "WaterSpell", 10, false, "water");
        DBCard defender = new DBCard("id2", "Knight", 10, true, "regular");

        //when
        float dmg = battleService.damageModifier(attacker, defender);

        //then
        assertEquals(10000, dmg);
    }

    @Test
    void damageModifierSpellVsKraken()
    {
        //given
        CardRepository cardRepositoryMock = mock(CardRepository.class);
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);

        BattleService battleService = new BattleService(cardRepositoryMock, userRepositoryMock, authorizationTokenHelperMock);

        DBCard attacker = new DBCard("id1", "RegularSpell", 10, false, "regular");
        DBCard defender = new DBCard("id2", "Kraken", 10, true, "regular");

        //when
        float dmg = battleService.damageModifier(attacker, defender);

        //then
        assertEquals(0, dmg);
    }

    @Test
    void damageModifierNormalSpellAttacksWater()
    {
        //given
        CardRepository cardRepositoryMock = mock(CardRepository.class);
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);

        BattleService battleService = new BattleService(cardRepositoryMock, userRepositoryMock, authorizationTokenHelperMock);

        DBCard attacker = new DBCard("id1", "RegularSpell", 10, false, "regular");
        DBCard defender = new DBCard("id2", "WaterElf", 10, true, "water");

        //when
        float dmg = battleService.damageModifier(attacker, defender);

        //then
        assertEquals(20, dmg);
    }

    @Test
    void damageModifierNormalSpellAttacksFire()
    {
        //given
        CardRepository cardRepositoryMock = mock(CardRepository.class);
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);

        BattleService battleService = new BattleService(cardRepositoryMock, userRepositoryMock, authorizationTokenHelperMock);

        DBCard attacker = new DBCard("id1", "RegularSpell", 10, false, "regular");
        DBCard defender = new DBCard("id2", "FireGoblin", 10, true, "fire");

        //when
        float dmg = battleService.damageModifier(attacker, defender);

        //then
        assertEquals(5, dmg);
    }

    @Test
    void damageModifierWaterSpellAttacksNormal()
    {
        //given
        CardRepository cardRepositoryMock = mock(CardRepository.class);
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);

        BattleService battleService = new BattleService(cardRepositoryMock, userRepositoryMock, authorizationTokenHelperMock);

        DBCard attacker = new DBCard("id1", "WaterSpell", 10, false, "water");
        DBCard defender = new DBCard("id2", "Dragon", 10, true, "regular");

        //when
        float dmg = battleService.damageModifier(attacker, defender);

        //then
        assertEquals(5, dmg);
    }
}