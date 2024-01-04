package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.UserStats;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ScoreboardService
{
    private final UserRepository userRepository;
    private final AuthorizationTokenHelper authorizationTokenHelper;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public ScoreboardService(UserRepository userRepository, AuthorizationTokenHelper authorizationTokenHelper)
    {
        this.userRepository = userRepository;
        this.authorizationTokenHelper = authorizationTokenHelper;
    }

    public Response getEloScoreboard(Request request)
    {
        if (authorizationTokenHelper.invalidToken(request))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        List<UserStats> scoreboard;
        scoreboard = userRepository.getEloScoreboard();

        String scoreboardJson;
        try
        {
            scoreboardJson  = objectMapper.writeValueAsString(scoreboard);
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseHelper.statusJsonBody(HttpStatus.OK, scoreboardJson);
    }
}
