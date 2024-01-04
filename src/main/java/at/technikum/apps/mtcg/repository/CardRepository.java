package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.MTCGDatabase;
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
    // t_card
    private final String CREATE_CARD = "INSERT INTO t_card (cardid, name, damage, monstertype, elementtype) VALUES (?,?,?,?,?)";
    private final String GET_CARD = "SELECT * FROM t_card WHERE cardid = ?";
    private final String SET_CARD_DAMAGE = "UPDATE t_card SET damage = ? WHERE cardId = ?";
    private final String ALL_CARDS_OF_USER = "SELECT cardid, name, damage FROM t_card WHERE owner = ?";
    private final String GET_CARD_OWNER = "SELECT owner FROM t_card WHERE cardid = ?";
    private final String DELETE_CARD = "DELETE FROM t_card WHERE cardid = ?";
    private final String DELETE_CARDS_TABLE = "DELETE FROM t_card";
    private final String CHANGE_CARD_OWNER = "UPDATE t_card SET owner = ? WHERE cardid = ?";

    // t_deck
    private final String CREATE_DECK = "INSERT INTO t_deck VALUES (?,?,?,?,?)";
    private final String GET_DECK = """
            SELECT tc.cardId, tc.name, tc.damage
            FROM t_deck td
            JOIN t_card tc ON td.cardId_1 = tc.cardId OR
                             td.cardId_2 = tc.cardId OR
                             td.cardId_3 = tc.cardId OR
                             td.cardId_4 = tc.cardId
            WHERE td.username = ?;
            """;
    private final String DELETE_DECKS_TABLE = "DELETE FROM t_deck";
    private final String IS_CARD_IN_DECK = """
            SELECT EXISTS (
                SELECT 1
                FROM t_deck
                WHERE ? IN (cardId_1, cardId_2, cardId_3, cardId_4)
            ) AS cardExists;
            """;
    private final String GET_DETAIL_DECK = """
            SELECT tc.cardId, tc.name, tc.damage, tc.monstertype, tc.elementtype
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
        con.close();
    }

    public List<RequestCard> getAllCardsOfUser(String username)
    {
        try
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
            con.close();
            return list;
        }
        catch (SQLException e)
        {
            return Collections.emptyList();
        }
    }

    public Optional<String> getCardOwner(String cardId)
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_CARD_OWNER);
            pstmt.setString(1, cardId);

            ResultSet rs = pstmt.executeQuery();
            con.close();
            if (rs.next())
            {
                return Optional.of(rs.getString("owner"));
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

    public void deleteAll() throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt2 = con.prepareStatement(DELETE_DECKS_TABLE);
        PreparedStatement pstmt = con.prepareStatement(DELETE_CARDS_TABLE);
        pstmt.execute();
        pstmt2.execute();
        con.close();
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
        con.close();
    }

    public List<RequestCard> getSimpleDeck(String username)
    {
        try
        {
            List<RequestCard> result = new ArrayList<>();
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_DECK);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            con.close();

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
        catch (SQLException e)
        {
            return Collections.emptyList();
        }
    }

    public List<DBCard> getDetailDeck(String username)
    {
        try
        {
            List<DBCard> result = new ArrayList<>();
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_DETAIL_DECK);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            con.close();

            while (rs.next())
            {
                result.add(new DBCard(
                        rs.getString("cardId"),
                        rs.getString("name"),
                        rs.getFloat("damage"),
                        rs.getBoolean("monstertype"),
                        rs.getString("elementtype")
                ));
            }
            return result;
        }
        catch (SQLException e)
        {
            return Collections.emptyList();
        }
    }

    public Optional<DBCard> getCard(String cardId)
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_CARD);
            pstmt.setString(1, cardId);

            ResultSet rs = pstmt.executeQuery();

            con.close();

            if(rs.next())
            {
                return Optional.of(new DBCard(
                        rs.getString("cardId"),
                        rs.getString("name"),
                        rs.getFloat("damage"),
                        rs.getBoolean("monstertype"),
                        rs.getString("elementtype")
                ));
            }
            return Optional.empty();
        }
        catch (SQLException e)
        {
            return Optional.empty();
        }
    }

    public void setCardDamage(String cardId, float damage) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(SET_CARD_DAMAGE);
        pstmt.setFloat(1, damage);
        pstmt.setString(2, cardId);
        pstmt.execute();
        con.close();
    }

    public void deleteCard(String cardId) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(DELETE_CARD);
        pstmt.setString(1, cardId);
        pstmt.execute();
        con.close();
    }

    public boolean isCardInDeck(String cardId)
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(IS_CARD_IN_DECK);
            pstmt.setString(1, cardId);
            ResultSet rs = pstmt.executeQuery();
            con.close();
            if (rs.next())
            {
                return rs.getBoolean("cardExists");
            }
            return false;
        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public void updateCardOwner(String cardid, String newOwner) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(CHANGE_CARD_OWNER);
        pstmt.setString(1, newOwner);
        pstmt.setString(2, cardid);
        pstmt.execute();
        con.close();
    }
}
