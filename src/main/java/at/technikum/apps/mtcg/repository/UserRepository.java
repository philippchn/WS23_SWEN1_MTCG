package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.UserDatabase;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository{

    private final String FIND_ALL_SQL = "SELECT * FROM usertable";

    private final String SAVE_SQL = "INSERT INTO usertable (username, password) VALUES(?, ?)";

    private final String FIND_BY_USERNAME = "SELECT * FROM usertable WHERE username = ?";

    private final String DELETE_ALL = "DELETE FROM usertable";

    private final String GET_USERDATA_BY_USERNAME = "SELECT * FROM userdatatable WHERE username = ?";

    private final String UPDATE_USERDATA_BY_USERNAME = "INSERT INTO userdatatable (username, name, bio, image) VALUES (?, ?, ?, ?) " +
            "ON CONFLICT (username) DO UPDATE SET name = EXCLUDED.name, bio = EXCLUDED.bio, image = EXCLUDED.image";

    private final UserDatabase userDatabase = new UserDatabase();

    public List<User> findAll() throws SQLException
    {
        List<User> users = new ArrayList<>();

        Connection con = userDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(FIND_ALL_SQL);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            User user = new User(
                    rs.getString("username"),
                    rs.getString("password")
            );
            users.add(user);
        }
        return users;
    }

    public void save(User user) throws SQLException
    {
        Connection con = userDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(SAVE_SQL);

        pstmt.setString(1, user.Username());
        pstmt.setString(2, user.Password());
        pstmt.execute();
    }

    public Optional<User> findUserByUsername(String username) throws SQLException
    {
        Connection con = userDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(FIND_BY_USERNAME);
        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next())
        {
            return Optional.of(new User(
                    rs.getString("username"),
                    rs.getString("password")
            ));
        }
        else
        {
            return Optional.empty();
        }
    }

    public Optional<UserData> findUserDataByUsername(String username) throws SQLException {
        Connection con = userDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(GET_USERDATA_BY_USERNAME);
        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next())
        {
            return Optional.of(new UserData(
                    rs.getString("name"),
                    rs.getString("bio"),
                    rs.getString("image")
            ));
        }
        else
        {
            return Optional.empty();
        }
    }

    public void updateUserDataByUsername(String username, UserData userData) throws SQLException {
        Connection con = userDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(UPDATE_USERDATA_BY_USERNAME);
        pstmt.setString(1, username);
        pstmt.setString(2, userData.Name());
        pstmt.setString(3, userData.Bio());
        pstmt.setString(4, userData.Image());
        pstmt.execute();
    }

    public void deleteAll() throws SQLException
    {
        Connection con = userDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(DELETE_ALL);
        pstmt.execute();
    }
}
