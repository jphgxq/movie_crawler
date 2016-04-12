package dao;

import com.douban.crawler.MovieInfo;
import com.mysql.jdbc.PreparedStatement;

import java.sql.SQLException;


public class MovieInfoDao extends Dao {

    public MovieInfoDao() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
    }

    public int insertMovieBasicInfo(MovieInfo movieInfo, String tab) {
        int status = -1;
        String insertData = "insert into " + tab + "(name, movie_href, poster_url, data_id)values(?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, movieInfo.name);
            pstmt.setString(2, movieInfo.movie_href);
            pstmt.setString(3, movieInfo.poster_url);
            pstmt.setString(4, movieInfo.data_id);
            status = pstmt.executeUpdate();
            conn.commit();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Cannot insert info into table" + tab);
        }
        return status;
    }

    public int updateMovieDetailInfo(MovieInfo movieInfo, String tab, String data_id) {
        int status = -1;
        String insertData = "UPDATE " + tab + " SET director = ?, adaptor = ?, actor = ?, type = ?, website = ?, display_date = ?, showing_time = ?, drama = ?, awards = ?, related_movie = ?, short_comment_url = ?, review_url = ?, score = ? WHERE (data_id) = " + data_id;
        try {
            PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(insertData);
            conn.setAutoCommit(false);
            pstmt.setString(1, movieInfo.director);
            pstmt.setString(2, movieInfo.adaptor);
            pstmt.setString(3, movieInfo.actor);
            pstmt.setString(4, movieInfo.type);
            pstmt.setString(5, movieInfo.website);
            pstmt.setString(6, movieInfo.display_date);
            pstmt.setString(7, movieInfo.showing_time);
            pstmt.setString(8, movieInfo.drama);
            pstmt.setString(9, movieInfo.awards);
            pstmt.setString(10, movieInfo.related_movie);
            pstmt.setString(11, movieInfo.short_comment_url);
            pstmt.setString(12, movieInfo.review_url);
            pstmt.setString(13, movieInfo.score);
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
