package dao;

import com.douban.crawler.TicketsMonthInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/2/27.
 */
public class TicketsMonthInfoDao extends Dao {

    public TicketsMonthInfoDao() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
    }

    public int insertTicketsMonthInfo(TicketsMonthInfo ticketsMonthInfo, String tab) {
        int status = -1;
        String insertData = "insert into " + tab + "(rank, movie_id, name, days, tickets_month, price_ave, people_ave, per_month, release_time, wom_index, date)values(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, String.valueOf(ticketsMonthInfo.rank));
            pstmt.setString(2, String.valueOf(ticketsMonthInfo.movie_id));
            pstmt.setString(3, ticketsMonthInfo.name);
            pstmt.setString(4, String.valueOf(ticketsMonthInfo.days));
            pstmt.setString(5, String.valueOf(ticketsMonthInfo.tickets_month));
            pstmt.setString(6, String.valueOf(ticketsMonthInfo.price_ave));
            pstmt.setString(7, String.valueOf(ticketsMonthInfo.people_ave));
            pstmt.setString(8, ticketsMonthInfo.per_month);
            pstmt.setString(9, ticketsMonthInfo.release_time);
            pstmt.setString(10, ticketsMonthInfo.wom_index);
            pstmt.setString(11, ticketsMonthInfo.date);
            status = pstmt.executeUpdate();
            conn.commit();
            pstmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.err.println("Cannot insert info into table " + tab);
        }
        return status;
    }
}
