package dao;

import com.douban.crawler.SessionTrendInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/2/26.
 */
public class SessionTrendInfoDao extends Dao {

    public SessionTrendInfoDao() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
    }

    public int updateNationalNum(SessionTrendInfo sessionTrendInfo, String tab) {
        int status = -1;
        String insertData = "insert into " + tab + "(movie_id, name, date, m_daysum)values(?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, String.valueOf(sessionTrendInfo.movie_id));
            pstmt.setString(2, sessionTrendInfo.name);
            pstmt.setString(3, sessionTrendInfo.date);
            pstmt.setString(4, String.valueOf(sessionTrendInfo.m_daysum));
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

    public int updateMovieNum(SessionTrendInfo sessionTrendInfo, String tab) {
        int status = -1;
        String insertData = "insert into " + tab + "(movie_id, name, date, m_daysum)values(?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, String.valueOf(sessionTrendInfo.movie_id));
            pstmt.setString(2, sessionTrendInfo.name);
            pstmt.setString(3, sessionTrendInfo.date);
            pstmt.setString(4, String.valueOf(sessionTrendInfo.m_daysum));
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
