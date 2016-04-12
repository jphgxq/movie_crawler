package dao;

import com.douban.crawler.TicketsHistoryInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/2/25.
 */
public class TicketsHistoryInfoDao extends Dao {

    public TicketsHistoryInfoDao() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
    }


    public int insertTicketsHistoryInfo(TicketsHistoryInfo ticketsHistoryInfo, String tab) {
        int status = -1;
        String insertData = "insert into " + tab + "(name, type, year, sum_tickets, price_ave, people_ave, area, date)values(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, ticketsHistoryInfo.name);
            pstmt.setString(2, ticketsHistoryInfo.type);
            pstmt.setString(3, String.valueOf(ticketsHistoryInfo.year));
            pstmt.setString(4, ticketsHistoryInfo.sum_tickets);
            pstmt.setString(5, ticketsHistoryInfo.price_ave);
            pstmt.setString(6, ticketsHistoryInfo.people_ave);
            pstmt.setString(7, ticketsHistoryInfo.area);
            pstmt.setString(8, ticketsHistoryInfo.date);
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
