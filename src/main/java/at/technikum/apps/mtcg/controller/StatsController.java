package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.StatsService;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class StatsController extends Controller{

    private final StatsService statsService = new StatsService(new UserRepository());
    @Override
    public boolean supports(String route)
    {
        return route.startsWith("/stats");
    }

    @Override
    public Response handle(Request request)
    {
        if (!request.getMethod().equals("GET"))
        {
            return status(HttpStatus.METHOD_NOT_ALLOWED);
        }
        return getUserStats(request);
    }

    public Response getUserStats(Request request)
    {
        return statsService.getUserStats(request);
    }
}
