package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.MTCGDatabase;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserData;
import at.technikum.apps.mtcg.entity.card.DBCard;
import at.technikum.apps.mtcg.entity.card.RequestCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CardRepository
{
    private final String CREATE_CARD = "INSERT INTO t_card (cardid, name, damage, monstertype, elementtype) VALUES (?,?,?,?,?)";
    private final String DELETE_CARDS = "DELETE FROM t_card";
    private final String DELETE_DECKS = "DELETE FROM t_deck";
    private final String ALL_CARDS_OF_USER = "SELECT cardid, name, damage FROM t_card WHERE owner = ?";
    private final String CREATE_DECK = "INSERT INTO t_deck VALUES (?,?,?,?,?)";
    private final String GET_CARD_OWNER = "SELECT owner FROM t_card WHERE cardid = ?";
    private final String GET_DECK = """
            SELECT tc.cardId, tc.name, tc.damage
            FROM t_deck td
            JOIN t_card tc ON td.cardId_1 = tc.cardId OR
                             td.cardId_2 = tc.cardId OR
                             td.cardId_3 = tc.cardId OR
                             td.cardId_4 = tc.cardId
            WHERE td.username = ?;
            """;
    private final MTCGDatabase MTCGDatabase = new MTCGDatabase();

    public void saveCard(DBCard dbCard) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(CREATE_CARD);

        pstmt.setString(1, dbCard.id());
        pstmt.setString(2, dbCard.name());
        pstmt.setFloat(3, dbCard.damage());
        pstmt.setBoolean(4, dbCard.isMonster());
        pstmt.setString(5, dbCard.elementType());
        pstmt.execute();
    }

    public List<RequestCard> getAllCardsOfUser(String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(ALL_CARDS_OF_USER);
        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();

        List<RequestCard> list = new ArrayList<>();

        while (rs.next())
        {
            list.add(new RequestCard(
                    rs.getString("cardid"),
                    rs.getString("name"),
                    rs.getInt("damage")
            ));
        }

        return list;
    }

    public Optional<String> getCardOwner(String cardId) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(GET_CARD_OWNER);
        pstmt.setString(1, cardId);

        ResultSet rs = pstmt.executeQuery();

        if (rs.next())
        {
            return Optional.of(rs.getString("owner"));
        }
        else
        {
            return Optional.empty();
        }
    }

    public void deleteAll() throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt2 = con.prepareStatement(DELETE_DECKS);
        PreparedStatement pstmt = con.prepareStatement(DELETE_CARDS);
        pstmt.execute();
        pstmt2.execute();
    }

    public void createDeck(String username, String[] ids) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(CREATE_DECK);
        pstmt.setString(1, username);
        pstmt.setString(2, ids[0]);
        pstmt.setString(3, ids[1]);
        pstmt.setString(4, ids[2]);
        pstmt.setString(5, ids[3]);

        pstmt.execute();
    }

    public List<RequestCard> getDeck(String username) throws SQLException
    {
        List<RequestCard> result = new ArrayList<>();
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(GET_DECK);
        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();

        while (rs.next())
        {
            result.add(new RequestCard(
                    rs.getString("cardId"),
                    rs.getString("name"),
                    rs.getFloat("damage")
            ));
        }
        return result;
    }
}
