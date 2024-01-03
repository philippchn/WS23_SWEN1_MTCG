package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.UserStats;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;

public class ScoreboardService
{
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public ScoreboardService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public Response getEloScoreboard(Request request)
    {
        if (AuthorizationTokenHelper.invalidToken(request))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        List<UserStats> scoreboard;
        String scoreboardJson;
        try
        {
            scoreboard = userRepository.getEloScoreboard();
            scoreboardJson  = objectMapper.writeValueAsString(scoreboard);
        }
        catch (SQLException | JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseHelper.statusJsonBody(HttpStatus.OK, scoreboardJson);
    }
}
