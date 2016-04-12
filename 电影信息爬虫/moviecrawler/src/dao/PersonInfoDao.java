package dao;

import com.douban.crawler.PersonInfo;

import com.mysql.jdbc.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Administrator on 2016/1/21.
 */
public class PersonInfoDao extends Dao {

    public PersonInfoDao() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
    }

    public int insertDirectorInfo(PersonInfo personInfo, String tab) {
        int status = -1;
        String insertData = "insert into " + tab + "(data_id, photo_url, name, info, intro, awards, latest_works, best_works, cooperator, fans)values(?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, personInfo.data_id);
            pstmt.setString(2, personInfo.photo_url);
            pstmt.setString(3, personInfo.name);
            pstmt.setString(4, personInfo.info);
            pstmt.setString(5, personInfo.intro);
            pstmt.setString(6, personInfo.awards);
            pstmt.setString(7, personInfo.latest_works);
            pstmt.setString(8, personInfo.best_works);
            pstmt.setString(9, personInfo.cooperator);
            pstmt.setString(10, personInfo.fans);
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
