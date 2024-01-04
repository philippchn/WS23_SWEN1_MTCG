package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.UserStats;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class StatsService
{
    private final UserRepository userRepository;
    private final AuthorizationTokenHelper authorizationTokenHelper;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public StatsService(UserRepository userRepository, AuthorizationTokenHelper authorizationTokenHelper)
    {
        this.userRepository = userRepository;
        this.authorizationTokenHelper = authorizationTokenHelper;
    }

    public Response getUserStats(Request request)
    {
        if (authorizationTokenHelper.invalidToken(request))
        {
            return ResponseHelper.status(HttpStatus.UNAUTHORIZED);
        }

        String username = authorizationTokenHelper.getUsernameFromToken(request);

        Optional<UserStats> userStats;
        userStats = userRepository.getUserStats(username);
        if (userStats.isEmpty())
        {
            return ResponseHelper.status(HttpStatus.NOT_FOUND);
        }

        String userStatsJson;
        try
        {
            userStatsJson = objectMapper.writeValueAsString(userStats.get());
        }
        catch (JsonProcessingException e)
        {
            return ResponseHelper.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseHelper.statusJsonBody(HttpStatus.OK, userStatsJson);
    }
}
