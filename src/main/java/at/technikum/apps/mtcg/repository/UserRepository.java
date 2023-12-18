package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.MTCGDatabase;
import at.technikum.apps.mtcg.entity.Token;
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

    private final String FIND_ALL_SQL = "SELECT * FROM t_user";

    private final String SAVE_SQL_USER = "INSERT INTO t_user (username, password) VALUES(?, ?)";

    private final String FIND_USER_BY_USERNAME = "SELECT * FROM t_user WHERE username = ?";
    private final String GET_COINS = "SELECT coins FROM t_user WHERE username = ?";

    private final String DELETE_ALL_FROM_USERTABLE = "DELETE FROM t_user";

    private final String DELETE_ALL_FROM_USERDATATABLE = "DELETE FROM t_userdata";

    private final String GET_USERDATA_BY_USERNAME = "SELECT * FROM t_userdata WHERE username = ?";
    private final String TAKE_FIVE_COINS_FROM_USER = "UPDATE t_user SET coins = coins - 5 WHERE username = ?";

    private final String UPDATE_USERDATA_BY_USERNAME = "INSERT INTO t_userdata (username, name, bio, image) VALUES (?, ?, ?, ?) " +
            "ON CONFLICT (username) DO UPDATE SET name = EXCLUDED.name, bio = EXCLUDED.bio, image = EXCLUDED.image";

    private final String LOGIN_USER = "UPDATE t_user SET token = ? WHERE username = ?";

    private final String GET_TOKEN = "SELECT token FROM t_user WHERE username = ?";

    private final MTCGDatabase MTCGDatabase = new MTCGDatabase();

    public List<User> findAll() throws SQLException
    {
        List<User> users = new ArrayList<>();

        Connection con = MTCGDatabase.getConnection();
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

    public void saveUser(User user) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(SAVE_SQL_USER);

        pstmt.setString(1, user.Username());
        pstmt.setString(2, user.Password());
        pstmt.execute();
    }

    public Optional<User> findUserByUsername(String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(FIND_USER_BY_USERNAME);
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

    public Optional<UserData> findUserDataByUsername(String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
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

    public void updateUserDataByUsername(String username, UserData userData) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(UPDATE_USERDATA_BY_USERNAME);
        pstmt.setString(1, username);
        pstmt.setString(2, userData.Name());
        pstmt.setString(3, userData.Bio());
        pstmt.setString(4, userData.Image());
        pstmt.execute();
    }

    public int getCoins(String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(GET_COINS);

        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt("coins");
    }

    public void takeFiveCoins(String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(TAKE_FIVE_COINS_FROM_USER);

        pstmt.setString(1, username);
        pstmt.execute();
    }

    public void loginUser(String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(LOGIN_USER);

        pstmt.setString(1, username + "-mtcgToken");
        pstmt.setString(2, username);
        pstmt.execute();
    }

    public Optional<Token> getTokenOfUser(String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(GET_TOKEN);

        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next())
        {
            return Optional.of(new Token(rs.getString("token")));
        }
        return Optional.empty();
    }

    public void deleteAll() throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();

        PreparedStatement pstmt = con.prepareStatement(DELETE_ALL_FROM_USERDATATABLE);
        pstmt.execute();

        PreparedStatement pstmt2 = con.prepareStatement(DELETE_ALL_FROM_USERTABLE);
        pstmt2.execute();
    }
}
