package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.MTCGDatabase;
import at.technikum.apps.mtcg.entity.card.DBCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CardRepository
{
    private final String CREATE_CARD = "INSERT INTO t_card (cardid, name, damage, monstertype, elementtype) VALUES (?,?,?,?,?)";
    private final String DELETE_CARDS = "DELETE FROM t_card";
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

    public void deleteAll() throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(DELETE_CARDS);
        pstmt.execute();
    }
}
