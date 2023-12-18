package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

public class SessionService {
    private final UserRepository userRepository;

    public SessionService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public Optional<Token> loginUser(User userFromRequest) throws SQLException
    {
        Optional<User> userFromDB = userRepository.findUserByUsername(userFromRequest.Username());

        if (userFromDB.isEmpty())
        {
            return Optional.empty();
        }

        if (userFromDB.get().Password().equals(userFromRequest.Password()))
        {
            loginUser(userFromDB.get().Username());
            return getTokenOfUser(userFromDB.get().Username());
        }
        return Optional.empty();
    }

    public void loginUser(String username) throws SQLException
    {
        userRepository.loginUser(username);
    }

    public Optional<Token> getTokenOfUser(String username) throws SQLException
    {
        return userRepository.getTokenOfUser(username);
    }
}
