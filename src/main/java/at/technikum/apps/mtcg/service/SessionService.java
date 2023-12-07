package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

public class SessionService {
    private final UserRepository userRepository = new UserRepository();

    public Optional<Token> getToken(User userFromRequest) throws SQLException {
        Optional<User> userFromDB = userRepository.findUserByUsername(userFromRequest.Username());

        if (userFromDB.isEmpty())
        {
            return Optional.empty();
        }

        if (checkPassword(userFromDB.get(), userFromRequest.Password()))
        {
            return Optional.ofNullable(generateToken(userFromDB.get()));
        }
        return Optional.empty();
    }

    boolean checkPassword(User user, String password)
    {
        return user.Password().equals(password);
    }

    Token generateToken(User user)
    {
        return new Token(user.Username() + "-mtcgToken");
    }
}
