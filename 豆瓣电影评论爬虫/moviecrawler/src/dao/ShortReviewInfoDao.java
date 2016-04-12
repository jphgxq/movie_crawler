package dao;

import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.douban.crawler.ShortReviewInfo;

public class ShortReviewInfoDao extends Dao {

	public ShortReviewInfoDao() throws ClassNotFoundException {
		super();
		// TODO Auto-generated constructor stub
	}

	public int insertBasicInfo(ShortReviewInfo moviebasicInfo, String tab){
		int status = -1;
		String insertData = "insert into " + tab + "(name, name_id, img_src, director, actors, country, time)values(?,?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
			conn.setAutoCommit(false);
			pstmt.setString(1, moviebasicInfo.name);
			pstmt.setString(2, moviebasicInfo.name_id);
			pstmt.setString(3, moviebasicInfo.img_src);
			pstmt.setString(4, moviebasicInfo.director);
			pstmt.setString(5, moviebasicInfo.actors);
			pstmt.setString(6, moviebasicInfo.country);
			pstmt.setString(7, moviebasicInfo.time);
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

	public int insertDetailedInfo(ShortReviewInfo moviebasicInfo, String tab, String name_id){
		int status = -1;
		String insertData = "UPDATE " + tab + " SET type = ?, ondisplay_date = ?, drama = ?, award = ?, score = ? WHERE (name_id) = " + name_id;
		try {
			PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
			conn.setAutoCommit(false);
			pstmt.setString(1, moviebasicInfo.type);
			pstmt.setString(2, moviebasicInfo.ondisplay_date);
			pstmt.setString(3, moviebasicInfo.drama);
			pstmt.setString(4, moviebasicInfo.award);
			pstmt.setString(5, moviebasicInfo.score);
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
