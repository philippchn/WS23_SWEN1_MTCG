package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;
import at.technikum.apps.mtcg.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository = new UserRepository();
    public List<User> findAll() throws SQLException
    {
        return userRepository.findAll();
    }

    public void save(User user) throws SQLException
    {
        userRepository.save(user);
    }

    public void deleteAll() throws SQLException
    {
        userRepository.deleteAll();
    }

    public Optional<UserData> getUserDataByUsername(String username) throws SQLException
    {
        return userRepository.findUserDataByUsername(username);
    }

    public void updateUserDataByUsername(String username, UserData userData) throws SQLException {
        userRepository.updateUserDataByUsername(username, userData);
    }
}
