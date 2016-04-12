package dao;

import com.douban.crawler.SessionInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/2/26.
 */
public class SessionInfoDao extends Dao {

    public SessionInfoDao() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
    }


    public int insertBasicInfo(SessionInfo sessionInfo, String tab) {
        int status = -1;
        String insertData = "insert into " + tab + "(movie_id, name, movie_num, per_num)values(?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, String.valueOf(sessionInfo.movie_id));
            pstmt.setString(2, sessionInfo.name);
            pstmt.setString(3, String.valueOf(sessionInfo.movie_num));
            pstmt.setString(4, sessionInfo.per_num);
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

    public int updateRenZhiIndex(SessionInfo sessionInfo, String tab, int movieid) {
        int status = -1;
        String insertData = "UPDATE " + tab + " SET renzhi_index = ? WHERE (movie_id) = " + movieid;
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, sessionInfo.renzhi_index);
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

    public int updateTicketIndex(SessionInfo sessionInfo, String tab, int movieid) {
        int status = -1;
        String insertData = "UPDATE " + tab + " SET tickets_index = ? WHERE (movie_id) = " + movieid;
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, sessionInfo.tickets_index);
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

    public int updateRapIndex(SessionInfo sessionInfo, String tab, int movieid) {
        int status = -1;
        String insertData = "UPDATE " + tab + " SET rap_index = ? WHERE (movie_id) = " + movieid;
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, sessionInfo.rap_index);
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
