package at.technikum.apps.mtcg.controller;

import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackageRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
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

        try
        {
            PackageRepository packageRepository = new PackageRepository();
            CardRepository cardRepository = new CardRepository();
            UserRepository userRepository = new UserRepository();
            packageRepository.deleteAll();
            cardRepository.deleteAll();
            userRepository.deleteAll();
        }
        catch (SQLException e)
        {
            return statusJsonBody(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return status(HttpStatus.OK);
    }
}
