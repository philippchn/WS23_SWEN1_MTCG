package at.technikum.apps.mtcg.controller;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    void supports()
    {
        Controller sessionController = new SessionController();
        Controller userController = new UserController();
        Controller transactionController = new TransactionController();
        Controller packageController = new PackageController();
        Controller deleteController = new DeleteController();
        Controller cardController = new CardController();
        Controller deckController = new DeckController();
        Controller statsController = new StatsController();
        Controller scoreboardController = new ScoreboardController();
        Controller battleController = new BattleController();
        Controller tradingController = new TradingController();

        assertTrue(sessionController.supports("/sessions"));
        assertTrue(userController.supports("/users"));
        assertTrue(transactionController.supports("/transactions"));
        assertTrue(packageController.supports("/packages"));
        assertTrue(deleteController.supports("/delete"));
        assertTrue(cardController.supports("/cards"));
        assertTrue(deckController.supports("/deck"));
        assertTrue(statsController.supports("/stats"));
        assertTrue(scoreboardController.supports("/scoreboard"));
        assertTrue(battleController.supports("/battles"));
        assertTrue(tradingController.supports("/tradings"));
    }
}