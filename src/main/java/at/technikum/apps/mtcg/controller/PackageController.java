package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.service.AuthorizationTokenHelper;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class PackageController extends Controller
{

    private final PackageService packageService = new PackageService(new PackageRepository(), new CardRepository(), new AuthorizationTokenHelper());

    @Override
    public boolean supports(String route)
    {
        return route.startsWith("/packages");
    }

    @Override
    public Response handle(Request request)
    {
        if (request.getMethod().equals("POST"))
        {
            return createPackage(request);
        }
        else
        {
            return status(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private Response createPackage(Request request)
    {
        return packageService.createPackage(request);
    }
}
