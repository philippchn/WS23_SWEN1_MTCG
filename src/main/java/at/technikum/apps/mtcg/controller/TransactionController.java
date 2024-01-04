package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.AuthorizationTokenHelper;
import at.technikum.apps.mtcg.service.TransactionService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class TransactionController extends Controller
{
    private final TransactionService transactionService = new TransactionService(new PackageRepository(), new UserRepository(), new AuthorizationTokenHelper());

    @Override
    public boolean supports(String route)
    {
        return route.startsWith("/transactions");
    }

    @Override
    public Response handle(Request request)
    {
        if (request.getMethod().equals("POST"))
        {
            return buyPackage(request);
        }
        else
        {
            return status(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private Response buyPackage(Request request)
    {
        return transactionService.buyPackage(request);
    }
}
