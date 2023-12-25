package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.CardService;
import at.technikum.apps.mtcg.service.PackageService;
import at.technikum.apps.mtcg.service.UserService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.sql.SQLException;

public class DeleteController extends Controller
{
    @Override
    public boolean supports(String route)
    {
        return route.startsWith("/delete");
    }

    @Override
    public Response handle(Request request)
    {
        UserService userService = new UserService(new UserRepository());
        PackageService packageService = new PackageService(new PackageRepository(), new CardRepository(), new UserRepository());
        CardService cardService = new CardService(new CardRepository());
        try
        {
            packageService.deleteAll();
            cardService.deleteAll();
            userService.deleteAll();
        }
        catch (SQLException e)
        {
            return statusJsonBody(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return status(HttpStatus.OK);
    }
}
