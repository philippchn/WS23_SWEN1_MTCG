package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.MTCGDatabase;
import at.technikum.apps.mtcg.entity.card.RequestCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PackageRepository
{
    private final String CREATE_PACKAGE = "INSERT INTO t_package (cardid_1, cardid_2, cardid_3, cardid_4, cardid_5) VALUES (?,?,?,?,?)";

    private final MTCGDatabase MTCGDatabase = new MTCGDatabase();

    public void savePackage(RequestCard[] requestCards) throws SQLException {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(CREATE_PACKAGE);

        pstmt.setString(1, requestCards[0].Id());
        pstmt.setString(2, requestCards[1].Id());
        pstmt.setString(3, requestCards[2].Id());
        pstmt.setString(4, requestCards[3].Id());
        pstmt.setString(5, requestCards[4].Id());
        pstmt.execute();
    }
}
