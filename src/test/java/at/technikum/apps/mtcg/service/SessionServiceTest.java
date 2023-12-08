package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class SessionServiceTest
{
    @Test
    void getToken() throws SQLException
    {
        //given
        UserRepository userRepository = mock(UserRepository.class);
        SessionService sessionService = new SessionService(userRepository);
        User user = new User("Name", "Password");

        when(userRepository.findUserByUsername("Name")).thenReturn(Optional.of(user));

        //when
        Optional<Token> token = sessionService.getToken(user);

        //then
        assertTrue(token.isPresent());
        assertEquals("Name-mtcgToken", token.get().token());
        verify(userRepository).findUserByUsername("Name");
    }
}