package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;
import at.technikum.apps.mtcg.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService
{
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public List<User> findAll() throws SQLException
    {
        return userRepository.findAll();
    }

    public Optional<User> findUserByUsername(String username) throws SQLException
    {
        return userRepository.findUserByUsername(username);
    }

    public void save(User user) throws SQLException
    {
        userRepository.saveUser(user);
    }

    public void deleteAll() throws SQLException
    {
        userRepository.deleteAll();
    }

    public Optional<UserData> getUserDataByUsername(String username) throws SQLException
    {
        return userRepository.findUserDataByUsername(username);
    }

    public void updateUserDataByUsername(String username, UserData userData) throws SQLException
    {
        userRepository.updateUserDataByUsername(username, userData);
    }

    public int getCoins(String username) throws SQLException
    {
        return userRepository.getCoins(username);
    }

    public void takeFiveCoins(String username) throws SQLException
    {
        userRepository.takeFiveCoins(username);
    }
}
