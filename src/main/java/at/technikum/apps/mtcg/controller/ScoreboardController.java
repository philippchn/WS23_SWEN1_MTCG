package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.AuthorizationTokenHelper;
import at.technikum.apps.mtcg.service.ScoreboardService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class ScoreboardController extends Controller{

    ScoreboardService scoreboardService = new ScoreboardService(new UserRepository(), new AuthorizationTokenHelper());

    @Override
    public boolean supports(String route)
    {
        return route.startsWith("/scoreboard");
    }

    @Override
    public Response handle(Request request)
    {
        if (!request.getMethod().equals("GET"))
        {
            return status(HttpStatus.METHOD_NOT_ALLOWED);
        }
        return getEloScoreboard(request);
    }

    private Response getEloScoreboard(Request request)
    {
        return scoreboardService.getEloScoreboard(request);
    }
}
