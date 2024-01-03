package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.MTCGDatabase;
import at.technikum.apps.mtcg.entity.card.RequestCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PackageRepository
{
    // t_package
    private final String CREATE_PACKAGE = "INSERT INTO t_package (cardid_1, cardid_2, cardid_3, cardid_4, cardid_5) VALUES (?,?,?,?,?)";
    private final String GET_ALL_AVAILABLE_PACKAGEID = "SELECT packageId FROM t_package WHERE available = true;";
    private final String SET_PACKAGE_UNAVAILABLE = "UPDATE t_package SET available = false WHERE packageId = ?";
    private final String GET_CARDIDS_FROM_PACKAGE = "SELECT cardid_1, cardid_2, cardid_3, cardid_4, cardid_5 FROM t_package WHERE packageid = ?";
    private final String DELETE_ALL_PACKAGE = "DELETE FROM t_package";
    private final String REMOVE_CARD_FROM_PACKAGE = "UPDATE t_package\n" +
            "SET\n" +
            "    cardId_1 = CASE WHEN cardId_1 = ? THEN NULL ELSE cardId_1 END,\n" +
            "    cardId_2 = CASE WHEN cardId_2 = ? THEN NULL ELSE cardId_2 END,\n" +
            "    cardId_3 = CASE WHEN cardId_3 = ? THEN NULL ELSE cardId_3 END,\n" +
            "    cardId_4 = CASE WHEN cardId_4 = ? THEN NULL ELSE cardId_4 END,\n" +
            "    cardId_5 = CASE WHEN cardId_5 = ? THEN NULL ELSE cardId_5 END\n" +
            "WHERE\n" +
            "    ? IN (cardId_1, cardId_2, cardId_3, cardId_4, cardId_5);\n";
    private final String SET_CARD_OWNER = "UPDATE t_card SET owner = ? WHERE cardid = ?";
    private final String GET_CARD_FROM_ID = "SELECT cardid, name, damage FROM t_card WHERE cardid = ?";

    private final MTCGDatabase MTCGDatabase = new MTCGDatabase();

    public void savePackage(RequestCard[] requestCards) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(CREATE_PACKAGE);

        pstmt.setString(1, requestCards[0].Id());
        pstmt.setString(2, requestCards[1].Id());
        pstmt.setString(3, requestCards[2].Id());
        pstmt.setString(4, requestCards[3].Id());
        pstmt.setString(5, requestCards[4].Id());
        pstmt.execute();
        con.close();
    }

    public List<Integer> getAllAvailablePackageId() throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(GET_ALL_AVAILABLE_PACKAGEID);

        ResultSet rs = pstmt.executeQuery();
        con.close();

        List<Integer> packageIds = new ArrayList<>();
        while (rs.next())
        {
            packageIds.add(rs.getInt("packageid"));
        }
        return packageIds;
    }

    public void buyPackage(String username, int packageId) throws SQLException {
        List<RequestCard> cards = getCardsFromPackage(packageId);

        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(SET_CARD_OWNER);
        for (RequestCard c : cards)
        {
            pstmt.setString(1, username);
            pstmt.setString(2, c.Id());

            pstmt.execute();
        }
        PreparedStatement pstmt2 = con.prepareStatement(SET_PACKAGE_UNAVAILABLE);
        pstmt2.setInt(1, packageId);

        pstmt2.execute();
        con.close();
    }

    public List<RequestCard> getCardsFromPackage(int packageId) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(GET_CARDIDS_FROM_PACKAGE);

        pstmt.setInt(1, packageId);
        ResultSet rs = pstmt.executeQuery();
        List<String> cardIds = new ArrayList<>();
        rs.next();
        cardIds.add(rs.getString("cardid_1"));
        cardIds.add(rs.getString("cardid_2"));
        cardIds.add(rs.getString("cardid_3"));
        cardIds.add(rs.getString("cardid_4"));
        cardIds.add(rs.getString("cardid_5"));

        List<RequestCard> cards = new ArrayList<>();

        PreparedStatement pstmtForCard = con.prepareStatement(GET_CARD_FROM_ID);
        for (String s : cardIds)
        {
            pstmtForCard.setString(1, s);
            ResultSet rsCard = pstmtForCard.executeQuery();
            rsCard.next();
            cards.add(new RequestCard(rsCard.getString("cardid"), rsCard.getString("name"), rsCard.getInt("damage")));
        }
        con.close();
        return cards;
    }

    public void deleteAll() throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement deletePackage = con.prepareStatement(DELETE_ALL_PACKAGE);

        deletePackage.execute();
        con.close();
    }

    public void removeCardFromPackage(String cardId) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(REMOVE_CARD_FROM_PACKAGE);
        pstmt.setString(1, cardId);
        pstmt.setString(2, cardId);
        pstmt.setString(3, cardId);
        pstmt.setString(4, cardId);
        pstmt.setString(5, cardId);
        pstmt.setString(6, cardId);

        pstmt.execute();
        con.close();
    }
}
