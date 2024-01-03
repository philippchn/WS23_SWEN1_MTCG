package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class BattleController extends Controller
{
    private final BattleService battleService = new BattleService(new CardRepository(), new UserRepository());
    private boolean isBattlePending = false;
    private Request enemyPending;

    @Override
    public boolean supports(String route)
    {
        return route.startsWith("/battles");
    }

    @Override
    public Response handle(Request request)
    {
        if (!request.getMethod().equals("POST"))
        {
            return status(HttpStatus.METHOD_NOT_ALLOWED);
        }
        if (battleService.invalidUser(request))
        {
            return status(HttpStatus.UNAUTHORIZED);
        }
        synchronized (this)
        {
            if (!isBattlePending)
            {
                isBattlePending = true;
                enemyPending = request;
                return statusJsonBody(HttpStatus.OK, "{\n\t\"response\": \"WAITING FOR OPPONENT\"\n}");
            }
            else
            {
                isBattlePending = false;
                return battleService.startBattle(request, enemyPending);
            }
        }
    }
}
