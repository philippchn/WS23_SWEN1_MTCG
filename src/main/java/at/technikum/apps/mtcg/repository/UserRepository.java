package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.MTCGDatabase;
import at.technikum.apps.mtcg.entity.Token;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;
import at.technikum.apps.mtcg.entity.UserStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserRepository{

    // t_user
    private final String FIND_ALL_SQL = "SELECT * FROM t_user";
    private final String SAVE_SQL_USER = "INSERT INTO t_user (username, password) VALUES(?, ?)";
    private final String FIND_USER_BY_USERNAME = "SELECT * FROM t_user WHERE username = ?";
    private final String GET_COINS = "SELECT coins FROM t_user WHERE username = ?";
    private final String DELETE_ALL_FROM_USERTABLE = "DELETE FROM t_user";
    private final String TAKE_FIVE_COINS_FROM_USER = "UPDATE t_user SET coins = coins - 5 WHERE username = ?";
    private final String LOGIN_USER = "UPDATE t_user SET token = ? WHERE username = ?";
    private final String GET_TOKEN = "SELECT token FROM t_user WHERE username = ?";

    // t_userdata
    private final String DELETE_USERDATA_TABLE = "DELETE FROM t_userdata";
    private final String GET_USERDATA_BY_USERNAME = "SELECT * FROM t_userdata WHERE username = ?";
    private final String UPDATE_USERDATA_BY_USERNAME = "INSERT INTO t_userdata (username, name, bio, image) VALUES (?, ?, ?, ?) " +
            "ON CONFLICT (username) DO UPDATE SET name = EXCLUDED.name, bio = EXCLUDED.bio, image = EXCLUDED.image";

    // t_stats
    private final String DELETE_STATS_TABLE = "DELETE FROM t_stats";
    private final String CREATE_EMPTY_USER_STATS = "INSERT INTO t_stats (name) VALUES (?)";
    private final String GET_USER_STATS = "SELECT * FROM t_stats WHERE name = ?";
    private final String GET_ELO_SCOREBOARD = "SELECT * FROM t_stats ORDER BY elo DESC";
    private final String GIVE_THREE_ELO = """
            UPDATE t_stats
            SET elo = elo + 3
            WHERE name = ?;
            """;
    private final String TAKE_FIVE_ELO = """
            UPDATE t_stats
            SET elo = elo - 5
            WHERE name = ?;
            """;

    private final MTCGDatabase MTCGDatabase = new MTCGDatabase();

    public List<User> findAll()
    {
        try
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
            con.close();
            return users;
        }
        catch (SQLException e)
        {
            return Collections.emptyList();
        }
    }

    public void saveUser(User user) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(SAVE_SQL_USER);

        pstmt.setString(1, user.Username());
        pstmt.setString(2, user.Password());
        pstmt.execute();

        PreparedStatement pstmt2 = con.prepareStatement(CREATE_EMPTY_USER_STATS);

        pstmt2.setString(1, user.Username());
        pstmt2.execute();
        con.close();
    }

    public Optional<User> findUserByUsername(String username)
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(FIND_USER_BY_USERNAME);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            con.close();
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
        catch (SQLException e)
        {
            return Optional.empty();
        }
    }

    public Optional<UserData> findUserDataByUsername(String username)
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_USERDATA_BY_USERNAME);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            con.close();
            if (rs.next())
            {
                return Optional.of(new UserData(
                        rs.getString("name"),
                        rs.getString("bio"),
                        rs.getString("image")
                ));
            }
            return Optional.empty();
        }
        catch (SQLException e)
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
        con.close();
    }

    public int getCoins(String username)
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_COINS);

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            rs.next();
            con.close();
            return rs.getInt("coins");
        }
        catch (SQLException e)
        {
            return 0;
        }
    }

    public void takeFiveCoins(String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(TAKE_FIVE_COINS_FROM_USER);

        pstmt.setString(1, username);
        pstmt.execute();
        con.close();
    }

    public void loginUser(String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(LOGIN_USER);

        pstmt.setString(1, username + "-mtcgToken");
        pstmt.setString(2, username);
        pstmt.execute();
        con.close();
    }

    public Optional<Token> getTokenOfUser(String username)
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_TOKEN);

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            con.close();
            if (rs.next())
            {
                return Optional.of(new Token(rs.getString("token")));
            }
            return Optional.empty();
        }
        catch (SQLException e)
        {
            return Optional.empty();
        }
    }

    public void deleteAll() throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();

        PreparedStatement pstmt = con.prepareStatement(DELETE_USERDATA_TABLE);
        pstmt.execute();

        PreparedStatement pstmt2 = con.prepareStatement(DELETE_STATS_TABLE);
        pstmt2.execute();

        PreparedStatement pstmt3 = con.prepareStatement(DELETE_ALL_FROM_USERTABLE);
        pstmt3.execute();
        con.close();
    }

    public Optional<UserStats> getUserStats(String username)
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_USER_STATS);

            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            con.close();
            if (rs.next())
            {
                return Optional.of(new UserStats(
                        rs.getString("name"),
                        rs.getInt("elo"),
                        rs.getInt("wins"),
                        rs.getInt("losses")
                ));
            }
            return Optional.empty();
        }
        catch (SQLException e)
        {
            return Optional.empty();
        }
    }

    public List<UserStats> getEloScoreboard()
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_ELO_SCOREBOARD);
            ResultSet rs = pstmt.executeQuery();
            con.close();

            List<UserStats> users = new ArrayList<>();
            while (rs.next())
            {
                users.add(new UserStats(
                        rs.getString("name"),
                        rs.getInt("elo"),
                        rs.getInt("wins"),
                        rs.getInt("losses")
                ));
            }
            return users;
        }
        catch (SQLException e)
        {
            return Collections.emptyList();
        }
    }

    public void giveThreeElo(String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(GIVE_THREE_ELO);

        pstmt.setString(1, username);
        pstmt.execute();
        con.close();
    }

    public void takeFiveElo(String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(TAKE_FIVE_ELO);

        pstmt.setString(1, username);
        pstmt.execute();
        con.close();
    }
}
