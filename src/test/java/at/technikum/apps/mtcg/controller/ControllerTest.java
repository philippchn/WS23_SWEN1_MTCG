package at.technikum.apps.mtcg.controller;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    void supports() {
        Controller sessionController = new SessionController();
        Controller userController = new UserController();
        Controller transactionController = new TransactionController();
        Controller packageController = new PackageController();

        assertTrue(sessionController.supports("/sessions"));
        assertTrue(userController.supports("/users"));
        assertTrue(transactionController.supports("/transactions"));
        assertTrue(packageController.supports("/packages"));
    }
}