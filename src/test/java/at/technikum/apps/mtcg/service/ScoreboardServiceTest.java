package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.UserStats;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScoreboardServiceTest
{

    @Test
    void getEloScoreboard()
    {
        //given
        UserRepository userRepositoryMock = mock(UserRepository.class);
        AuthorizationTokenHelper authorizationTokenHelperMock = mock(AuthorizationTokenHelper.class);
        ScoreboardService scoreboardService = new ScoreboardService(userRepositoryMock, authorizationTokenHelperMock);

        Request request = new Request();
        request.setAuthorizationToken("username-mtcgToken");
        List<UserStats> scoreboard = List.of(
                new UserStats("name1", 5, 5, 0),
                new UserStats("name2", 2, 2, 0)
        );

        when(authorizationTokenHelperMock.invalidToken(any())).thenReturn(false);
        when(userRepositoryMock.getEloScoreboard()).thenReturn(scoreboard);

        //when
        Response response = scoreboardService.getEloScoreboard(request);

        //then
        assertEquals("[{\"Name\":\"name1\",\"Elo\":5,\"Wins\":5,\"Losses\":0},{\"Name\":\"name2\",\"Elo\":2,\"Wins\":2,\"Losses\":0}]",
                response.getBody());
    }
}