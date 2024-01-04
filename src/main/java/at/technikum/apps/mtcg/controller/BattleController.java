package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.AuthorizationTokenHelper;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class BattleController extends Controller
{
    private final BattleService battleService = new BattleService(new CardRepository(), new UserRepository(), new AuthorizationTokenHelper());
    private boolean isBattlePending = false;
    private Request enemyPending;
    private String battleLog = "";

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
                return requestBattle(request);
            }
            else
            {
                if (request.getAuthorizationToken().equals(enemyPending.getAuthorizationToken()))
                {
                    return status(HttpStatus.FORBIDDEN);
                }
                return prepareBattle(request);
            }
        }
    }

    private Response requestBattle(Request request)
    {
        battleLog = "";
        isBattlePending = true;
        enemyPending = request;

        try
        {
            long timeoutMillis = 5000;
            long startTime = System.currentTimeMillis();

            while (battleLog.isEmpty())
            {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;

                if (elapsedTime >= timeoutMillis) {
                    isBattlePending = false;
                    return status(HttpStatus.REQUEST_TIMEOUT);
                }

                this.wait(timeoutMillis - elapsedTime);
            }
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (battleLog.equals("ERROR"))
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return statusJsonBody(HttpStatus.OK, battleLog);
    }

    private Response prepareBattle(Request request)
    {
        isBattlePending = false;
        battleLog = battleService.startBattle(request, enemyPending);
        this.notify();
        if (battleLog.equals("ERROR"))
        {
            return status(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return statusJsonBody(HttpStatus.OK, battleLog);
    }
}
