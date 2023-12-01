package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.UserDatabase;
import at.technikum.apps.mtcg.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository{

    private final String FIND_ALL_SQL = "SELECT * FROM usertable";

    private final String SAVE_SQL = "INSERT INTO usertable (username, password) VALUES(?, ?)";

    private final UserDatabase userDatabase = new UserDatabase();
    public List<User> findAll()
    {
        List<User> users = new ArrayList<>();

        try (
                Connection con = userDatabase.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ALL_SQL);
                ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {
                User user = new User(
                        rs.getString("name"),
                        rs.getString("password")
                );
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            return users;
        }
    }

    public User save(User user) throws SQLException {
        Connection con = userDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(SAVE_SQL);

        pstmt.setString(1, user.username());
        pstmt.setString(2, user.password());
        pstmt.execute();

        return user;
    }

    // TODO
    User findByUsername(String username)
    {
        return null;
    }

}
