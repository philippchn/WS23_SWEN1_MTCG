package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.MTCGDatabase;
import at.technikum.apps.mtcg.entity.Trade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TradingRepository
{
    // t_trades
    private final String GET_ALL_TRADES = "SELECT * FROM t_trades";
    private final String CREATE_TRADE = "INSERT INTO t_trades (id, cardid, type, mindmg, owner) VALUES (?, ?, ?, ?, ?)";
    private final String DELETE_TRADE = "DELETE FROM t_trades WHERE id = ?";
    private final String GET_TRADE_OWNER = "SELECT owner FROM t_trades WHERE id = ?";
    private final String GET_TRADE_MINDMG = "SELECT mindmg FROM t_trades WHERE id = ?";
    private final String GET_CARD_ID = "SELECT cardid FROM t_trades WHERE id = ?";
    private final String DELETE_TRADES_TABLE = "DELETE FROM t_trades";

    private final at.technikum.apps.mtcg.data.MTCGDatabase MTCGDatabase = new MTCGDatabase();

    public List<Trade> getAvailableTrades()
    {
        try
        {
            List<Trade> result = new ArrayList<>();
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_ALL_TRADES);

            ResultSet rs = pstmt.executeQuery();

            con.close();

            while (rs.next())
            {
                result.add(new Trade(
                        rs.getString("id"),
                        rs.getString("cardid"),
                        rs.getString("type"),
                        rs.getFloat("mindmg")
                ));
            }
            return result;
        }
        catch (SQLException e)
        {
            return Collections.emptyList();
        }
    }

    public void createTrade(Trade trade, String username) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(CREATE_TRADE);

        pstmt.setString(1, trade.Id());
        pstmt.setString(2, trade.CardToTrade());
        pstmt.setString(3, trade.Type());
        pstmt.setFloat(4, trade.MinimumDamage());
        pstmt.setString(5, username);

        pstmt.execute();
        con.close();
    }

    public void deleteTrade(String tradingDealId) throws SQLException
    {
        Connection con = MTCGDatabase.getConnection();
        PreparedStatement pstmt = con.prepareStatement(DELETE_TRADE);

        pstmt.setString(1, tradingDealId);

        pstmt.execute();
        con.close();
    }

    public Optional<String> getTradeOwner(String tradingDealId)
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_TRADE_OWNER);

            pstmt.setString(1, tradingDealId);

            ResultSet rs = pstmt.executeQuery();
            con.close();

            if (rs.next())
            {
                return Optional.of(rs.getString("owner"));
            }
            return Optional.empty();
        }
        catch (SQLException e)
        {
            return Optional.empty();
        }
    }

    public Optional<Float> getMindmg(String tradingDealId)
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_TRADE_MINDMG);

            pstmt.setString(1, tradingDealId);

            ResultSet rs = pstmt.executeQuery();
            con.close();

            if (rs.next())
            {
                return Optional.of(rs.getFloat("mindmg"));
            }
            return Optional.empty();
        }
        catch (SQLException e)
        {
            return Optional.empty();
        }
    }

    public Optional<String> getCardIdFromTrade(String tradeid)
    {
        try
        {
            Connection con = MTCGDatabase.getConnection();
            PreparedStatement pstmt = con.prepareStatement(GET_CARD_ID);

            pstmt.setString(1, tradeid);

            ResultSet rs = pstmt.executeQuery();
            con.close();

            if (rs.next())
            {
                return Optional.of(rs.getString("cardid"));
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
        PreparedStatement pstmt = con.prepareStatement(DELETE_TRADES_TABLE);

        pstmt.execute();
        con.close();
    }
}
