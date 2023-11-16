package at.technikum.apps.mtcg.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    void supports() {
        Controller battleController = new BattleController();
        Controller cardController = new CardController();
        Controller deckController = new DeckController();
        Controller packageController = new PackageController();
        Controller scoreboardController = new ScoreboardController();
        Controller sessionController = new SessionController();
        Controller statController = new StatController();
        Controller tradingController = new TradingController();
        Controller transactionController = new TransactionController();
        Controller userController = new UserController();

        assertTrue(battleController.supports("/battles"));
        assertTrue(cardController.supports("/cards"));
        assertTrue(deckController.supports("/decks"));
        assertTrue(packageController.supports("/packages"));
        assertTrue(scoreboardController.supports("/scoreboards"));
        assertTrue(sessionController.supports("/sessions"));
        assertTrue(statController.supports("/stats"));
        assertTrue(tradingController.supports("/tradings"));
        assertTrue(transactionController.supports("/transactions"));
        assertTrue(userController.supports("/users"));
    }
}