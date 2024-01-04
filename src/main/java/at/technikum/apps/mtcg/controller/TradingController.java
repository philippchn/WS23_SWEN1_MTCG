package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.TradingRepository;
import at.technikum.apps.mtcg.service.AuthorizationTokenHelper;
import at.technikum.apps.mtcg.service.TradingService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class TradingController extends Controller
{
    private final TradingService tradingService = new TradingService(new TradingRepository(), new CardRepository(), new AuthorizationTokenHelper());

    @Override
    public boolean supports(String route)
    {
        return route.startsWith("/tradings");
    }

    @Override
    public Response handle(Request request)
    {
        if (request.getRoute().equals("/tradings"))
        {
            return switch (request.getMethod()) {
                case "GET" -> getAvailableTrades();
                case "POST" -> createTrade(request);
                default ->
                        status(HttpStatus.METHOD_NOT_ALLOWED);
            };
        }
        else
        {
            return switch (request.getMethod()) {
                case "DELETE" -> deleteTrade(request);
                case "POST" -> doTrade(request);
                default ->
                        status(HttpStatus.METHOD_NOT_ALLOWED);
            };
        }
    }

    private Response getAvailableTrades()
    {
        return tradingService.getAvailableTrades();
    }

    private Response createTrade(Request request)
    {
        return tradingService.createTrade(request);
    }

    private Response deleteTrade(Request request)
    {
        return tradingService.deleteTrade(request);
    }

    private Response doTrade(Request request)
    {
        return tradingService.doTrade(request);
    }
}
