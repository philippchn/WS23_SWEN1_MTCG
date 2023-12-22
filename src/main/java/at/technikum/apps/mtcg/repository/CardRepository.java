package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.MTCGDatabase;
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
    private final String ALL_CARDS_OF_USER = """
            SELECT tc.cardid, tc.name, tc.damage FROM t_userToPackage tup JOIN t_package tp ON tup.packageId = tp.packageId\s
            JOIN t_card tc ON tp.cardId_1 = tc.cardId OR
                              tp.cardId_2 = tc.cardId OR
                              tp.cardId_3 = tc.cardId OR
                              tp.cardId_4 = tc.cardId OR
                              tp.cardId_5 = tc.cardId
            WHERE tup.username = ?;""";
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

    public void deleteAll() throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(DELETE_CARDS);
        pstmt.execute();
    }
}
