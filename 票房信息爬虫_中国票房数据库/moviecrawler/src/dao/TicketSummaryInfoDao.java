package dao;

import com.douban.crawler.TicketSummaryInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;


public class TicketSummaryInfoDao extends Dao {

    public TicketSummaryInfoDao() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
    }

    public int insertTicketSummaryInfo(TicketSummaryInfo ticketSummaryInfo, String tab) {
        int status = -1;
        String insertData = "insert into " + tab + "(rankYear, rankHistory, name, ticketSummary, peopleSummary, sessionSummary, displayDate)values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, ticketSummaryInfo.rankYear);
            pstmt.setString(2, ticketSummaryInfo.rankHistory);
            pstmt.setString(3, ticketSummaryInfo.name);
            pstmt.setString(4, ticketSummaryInfo.ticketSummary);
            pstmt.setString(5, ticketSummaryInfo.peopleSummary);
            pstmt.setString(6, ticketSummaryInfo.sessionSummary);
            pstmt.setString(7, ticketSummaryInfo.displayDate);
            status = pstmt.executeUpdate();
            conn.commit();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Cannot insert info into table" + tab);
        }
        return status;
    }

}
