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
        UserRepository userRepositoryMock = mock(UserRepository.class);
        SessionService sessionService = new SessionService(userRepositoryMock);
        User user = new User("Name", "Password");
        Token token = new Token("Name-mtcgToken");

        when(userRepositoryMock.findUserByUsername("Name")).thenReturn(Optional.of(user));
        when(userRepositoryMock.getTokenOfUser("Name")).thenReturn(Optional.of(token));
        doNothing().when(userRepositoryMock).loginUser("Name");

        //when
        Optional<Token> resultToken = sessionService.loginUser(user);

        //then
        assertTrue(resultToken.isPresent());
        assertEquals("Name-mtcgToken", resultToken.get().token());
        verify(userRepositoryMock).findUserByUsername("Name");
    }
}