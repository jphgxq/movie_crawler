package com.douban.crawler;

import dao.CommentInfoDao;
import dao.MovieInfoDao;
import dao.PersonInfoDao;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MovieCrawler {

	MovieInfo movieInfo;
	PersonInfo personInfo;
	CommentInfo commentInfo;
	MovieInfoDao movieInfodao;
	PersonInfoDao personInfodao;
	CommentInfoDao commentInfoDao;

	private int id;
	public MovieCrawler() {
		// TODO Auto-generated constructor stub
	}

	//豆瓣首页选电影部分可获取到电影的名称、海报图片链接地址、电影详细信息链接地址、data_id唯一标识电影
	public void crawler(String tab) throws ClassNotFoundException, JSONException {
		movieInfodao = new MovieInfoDao();
		personInfodao = new PersonInfoDao();
		commentInfoDao = new CommentInfoDao();
		movieInfo = new MovieInfo();
		String url = "https://movie.douban.com/j/search_subjects?type=movie&tag=%E7%83%AD%E9%97%A8&sort=recommend&page_limit=400&page_start=0";
		Document doc = null;
		doc = this.getHtmlContent(url);
		String html = doc.select("body").text();
		JSONObject obj = new JSONObject(html);
		JSONArray jsonArray = (JSONArray) obj.get("subjects");
		for(int i=0;i<jsonArray.length();i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			movieInfo.name = jsonObject.getString("title");
			movieInfo.movie_href = jsonObject.getString("url");
			movieInfo.poster_url = jsonObject.getString("cover");
			movieInfo.data_id = jsonObject.getString("id");
//			movieInfodao.insertMovieBasicInfo(movieInfo, tab);
			Document movieDetailDoc = this.getHtmlContent(movieInfo.movie_href);
			this.getMovieDetailInfo(movieDetailDoc, tab, movieInfo.data_id);
			if(i%10 == 0) {
				try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.out.println("Error:" + e.getMessage());
				}
			}
		}
	}

	//获取由电影链接地址得到网页中的内容
	public void getMovieDetailInfo(Document doc, String tab, String data_id) {
		movieInfo = new MovieInfo();
		if (doc!=null) {
			movieInfo.director = doc.select("div[id=\"info\"]").select("a[rel=\"v:directedBy\"]").text();
			String href = doc.select("div[id=\"info\"]").select("a[rel=\"v:directedBy\"]").attr("href");
			String directorUrl = "https://movie.douban.com" + href;
			Document directorDoc = this.getHtmlContent(directorUrl);
//			this.getDirectorInfo(directorDoc, "person_info", data_id);
			movieInfo.adaptor = doc.select("div[id=\"info\"]").select("span:contains(编剧)").select("span[class=\"attrs\"]").select("a").text();
			movieInfo.actor = doc.select("div[id=\"info\"]").select("span[class=\"actor\"]").select("span[class=\"attrs\"]").select("span").text();
			movieInfo.type = doc.select("div[id=\"info\"]").select("span[property=\"v:genre\"]").text();
			try {
				String str = doc.select("div[id=\"info\"]").select("a[rel=\"nofollow\"]").first().text();
				if (str!=null) {
					if (str.substring(0,2).equals("tt")) {
						movieInfo.website = null;
					} else {
						movieInfo.website = str;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				movieInfo.website = null;
			}
			movieInfo.display_date = doc.select("div[id=\"info\"]").select("span[property=\"v:initialReleaseDate\"]").text();
			movieInfo.showing_time = doc.select("div[id=\"info\"]").select("span[property=\"v:runtime\"]").text();
			movieInfo.drama = doc.select("span[property=\"v:summary\"]").text();
			movieInfo.awards = doc.select("div.mod").select("ul.award").text();
			movieInfo.related_movie = doc.select("div.recommendations-bd").select("a").text();
			movieInfo.short_comment_url = doc.select("div[id=\"comments-section\"]").select("div.mod-hd").first().select("span.pl").select("a").attr("href");
			Document shortCommentDoc = this.getHtmlContent(movieInfo.short_comment_url);
			this.getShortCommentInfo(shortCommentDoc, "comment_info", data_id);
			movieInfo.review_url = doc.select("div[id=\"review_section\"]").select("div.mod-hd").first().select("span.pl").select("a").attr("href");
			movieInfo.score = doc.select("div[id=\"interest_sectl\"]").select("strong[property=\"v:average\"]").text();
//			movieInfodao.updateMovieDetailInfo(movieInfo, tab, data_id);
		}
	}

	//获取导演的相关信息
	public void getDirectorInfo(Document doc, String tab, String data_id){
		personInfo = new PersonInfo();
		if (doc!=null) {
			personInfo.data_id = data_id;
			personInfo.name = doc.select("div[id=\"content\"]").select("h1").first().text();
			personInfo.photo_url = doc.select("div.article").select("div[id=\"headline\"]").select("div.pic").select("a").attr("href");
			personInfo.info = doc.select("div.article").select("div[id=\"headline\"]").select("div.info").select("li").text();
			personInfo.intro = doc.select("div[id=\"intro\"]").select("div.bd").select("span[class=\"all hidden\"]").text();
			personInfo.awards = doc.select("ul[class=\"award\"]").text();
			personInfo.latest_works = doc.select("div[id=\"recent_movies\"]").select("div.bd").select("ul.list-s").select("li").text();
			personInfo.best_works = doc.select("div[id=\"best_movies\"]").select("div.bd").select("ul.list-s").select("li").text();
			personInfo.cooperator = doc.select("div[id=\"partners\"]").select("div.bd").select("ul.list-s").select("li").text();
			personInfo.fans = doc.select("div.aside").select("div[id=\"fans\"]").select("ul.list-s").select("li").text();
//			personInfodao.insertDirectorInfo(personInfo, tab);
		}
	}

	//获取短评信息
	public void getShortCommentInfo(Document doc, String tab, String data_id){
		commentInfo = new CommentInfo();
		if (doc!=null) {
			commentInfo.data_id = data_id;
			Elements commentlist = doc.select("div.comment-item");
			for (Element element : commentlist) {
				commentInfo.srid = element.attr("data-cid");
				commentInfo.reviewer = element.select("span.comment-info").select("a").text();
				commentInfo.rank = element.select("span.comment-info").select("span").attr("title");
				commentInfo.stars = Converter(commentInfo.rank) + "";
				commentInfo.date = element.select("span.comment-info").select("span[class=\"\"]").text();
				commentInfo.votes = element.select("span[class=\"votes pr5\"]").text();
				commentInfo.comment = element.select("p").text();
				commentInfoDao.insertCommentInfo(commentInfo, tab);
				String reviewerUrl = element.select("span.comment-info").select("a").attr("href");
				try {
					Document reviewerdoc = this.getHtmlContent(reviewerUrl);
					this.getReviewerInfo(reviewerdoc, tab, commentInfo.srid);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	//获取评论者信息
	private void getReviewerInfo(Document doc, String tab, String srid) {
		commentInfo = new CommentInfo();
		commentInfo.habitual_residence = doc.select("div[class=\"user-info\"]").select("a").text();
		commentInfo.join_time = doc.select("div[class=\"user-info\"]").select("div[class=\"pl\"]").text();
		commentInfo.account = doc.select("div[class=\"user-intro\"]").select("textarea").text();
		commentInfoDao.updateReviewerInfo(commentInfo, tab, srid);
	}

	//将rank值转化为推荐值
	private int Converter(String rank){
		int stars = 0;

		System.out.println(rank.length()+" "+rank);
		if (rank.equals("力荐")) {
			stars = 50;
		}
		else if (rank.equals("推荐")) {
			stars = 40;
		}
		else if (rank.equals("还行")) {
			stars = 30;
		}
		else if (rank.equals("较差")) {
			stars = 20;
		}
		else if (rank.equals("很差")) {
			stars = 10;
		}
		System.out.println(rank+" "+stars);

		return stars;
	}

	public Document getHtmlContent(String url){
		Document doc = null;
		while(true){
			try {
				doc = Jsoup.connect(url)
				.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36")
				.timeout(10*1000)
				.ignoreContentType(true)
				.get();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return doc;
	}
	
	public static void main(String[] args) {
		MovieCrawler srCrawler = new MovieCrawler();
		try {
			srCrawler.crawler("movie_info");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
