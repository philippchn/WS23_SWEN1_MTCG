package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.UserStats;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatsServiceTest
{

    @Test
    void getUserStats()
    {
        //given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);
        StatsService statsService = new StatsService(userRepositoryMock, authorizationTokenHelperMock);

        Request request = new Request();
        String username = "username";
        UserStats userStats = new UserStats("username", 20,5, 5);

        when(authorizationTokenHelperMock.invalidToken(any())).thenReturn(false);
        when(authorizationTokenHelperMock.getUsernameFromToken(request)).thenReturn("username");
        when(userRepositoryMock.getUserStats(username)).thenReturn(Optional.of(userStats));

        //when
        Response response = statsService.getUserStats(request);

        //then
        assertEquals("{\"Name\":\"username\",\"Elo\":20,\"Wins\":5,\"Losses\":5}", response.getBody());
    }
}