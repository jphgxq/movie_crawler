package dao;

import com.douban.crawler.CommentInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;

/**
 * Created by Administrator on 2016/1/21.
 */
public class CommentInfoDao extends Dao{

    public CommentInfoDao() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
    }

    public int insertCommentInfo(CommentInfo commentInfo, String tab) {
        int status = -1;
        String insertData = "insert into " + tab + "(data_id, srid, reviewer, rank, stars, date, votes, comment)values(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, commentInfo.data_id);
            pstmt.setString(2, commentInfo.srid);
            pstmt.setString(3, commentInfo.reviewer);
            pstmt.setString(4, commentInfo.rank);
            pstmt.setString(5, commentInfo.stars);
            pstmt.setString(6, commentInfo.date);
            pstmt.setString(7, commentInfo.votes);
            pstmt.setString(8, commentInfo.comment);
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

    public int updateReviewerInfo(CommentInfo commentInfo, String tab, String srid) {
        int status = -1;
        String insertData = "UPDATE " + tab + " SET habitual_residence = ?, join_time = ?, account = ? WHERE (srid) = " + srid;
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, commentInfo.habitual_residence);
            pstmt.setString(2, commentInfo.join_time);
            pstmt.setString(3, commentInfo.account);
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
