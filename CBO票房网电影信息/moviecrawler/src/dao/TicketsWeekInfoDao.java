package dao;

import com.douban.crawler.TicketsWeekInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/2/27.
 */
public class TicketsWeekInfoDao extends Dao {

    public TicketsWeekInfoDao() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
    }

    public int insertTicketsWeekInfo(TicketsWeekInfo ticketsWeekInfo, String tab) {
        int status = -1;
        String insertData = "insert into " + tab + "(rank, movie_id, name, tickets_week, tickets_sum, days, price_ave, people_ave, wom_index)values(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, String.valueOf(ticketsWeekInfo.rank));
            pstmt.setString(2, String.valueOf(ticketsWeekInfo.movie_id));
            pstmt.setString(3, ticketsWeekInfo.name);
            pstmt.setString(4, String.valueOf(ticketsWeekInfo.tickets_week));
            pstmt.setString(5, String.valueOf(ticketsWeekInfo.tickets_sum));
            pstmt.setString(6, String.valueOf(ticketsWeekInfo.days));
            pstmt.setString(7, String.valueOf(ticketsWeekInfo.price_ave));
            pstmt.setString(8, String.valueOf(ticketsWeekInfo.people_ave));
            pstmt.setString(9, ticketsWeekInfo.wom_index);
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
