package dao;

import java.sql.SQLException;

import com.douban.crawler.ShortCommentInfo;
import com.mysql.jdbc.PreparedStatement;
import com.douban.crawler.ShortReviewInfo;

public class ShortCommentInfoDao extends Dao {

    public ShortCommentInfoDao() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
    }

    public int insertShortComment(ShortCommentInfo srInfo, String tab){
        int status = -1;
        String insertData = "insert into " + tab + "(srid, name_id, reviewer, rank, stars, date, votes, comment)values(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, srInfo.srid);
            pstmt.setString(2, srInfo.name_id);
            pstmt.setString(3, srInfo.reviewer);
            pstmt.setString(4, srInfo.rank);
            pstmt.setString(5, srInfo.stars);
            pstmt.setString(6, srInfo.date);
            pstmt.setString(7, srInfo.votes);
            pstmt.setString(8, srInfo.comment);
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

    public int insertReviewerInfo(ShortCommentInfo srInfo, String tab, String srid){
        int status = -1;
        String insertData = "UPDATE " + tab + " SET account = ?, city = ?, sign_time = ? WHERE (srid) = " + srid;
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, srInfo.account);
            pstmt.setString(2, srInfo.city);
            pstmt.setString(3, srInfo.sign_time);
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
