package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class UserService {
    private final UserRepository userRepository = new UserRepository();
    public List<User> findAll() throws SQLException {
        return userRepository.findAll();
    }

    public User save(User user) throws SQLException {
        return userRepository.save(user);
    }
}
