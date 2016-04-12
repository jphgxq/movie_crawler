package com.douban.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import dao.ShortReviewInfoDao;
import dao.ShortCommentInfoDao;

public class ShortReviewCrawler {
	
	ShortReviewInfo moviebasicInfo;
	ShortCommentInfo srInfo;
	ShortReviewInfoDao srdao;
	ShortCommentInfoDao comdao;
	private int id;
	public ShortReviewCrawler() {
		// TODO Auto-generated constructor stub
	}

	public void crawlShortReview(String mid, String tab) throws ClassNotFoundException {
		srdao = new ShortReviewInfoDao();
		comdao = new ShortCommentInfoDao();
		String url = "http://movie.douban.com/nowplaying/xian/";
		Document doc = null;
		doc = this.getHtmlContent(url);
		this.getMovieBasicInfo(doc, mid, tab);
	}

	private void getMovieBasicInfo(Document doc, String mid, String tab){
		if (doc!=null) {
			id = 1;
			Elements movielist = doc.select("ul.lists").select("li[class=\"list-item\"]");
			for (Element element : movielist) {
				moviebasicInfo = new ShortReviewInfo();
				//data-cid
				moviebasicInfo.name = element.attr("data-title");
				moviebasicInfo.name_id = element.attr("id");
				moviebasicInfo.img_src = element.select("img").attr("src");
				moviebasicInfo.director = element.attr("data-director");
				moviebasicInfo.actors = element.attr("data-actors");
				moviebasicInfo.country = element.attr("data-region");
				moviebasicInfo.time = element.attr("data-duration");
//				srdao.insertBasicInfo(moviebasicInfo, tab);
				String url = element.select("a").attr("href");
				Document detaildoc = this.getHtmlContent(url);
				this.getDetailedInfo(detaildoc, mid, tab, moviebasicInfo.name_id);
				id++;
				if (id%10 == 0) {
					try {
						Thread.sleep(1*1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("Error:"+e1.getMessage());
					}
				}
			}
		}
	}

	private void getDetailedInfo(Document doc, String mid, String tab, String name_id) {
		if (doc != null) {
			moviebasicInfo = new ShortReviewInfo();
			Elements type = doc.select("span[property=\"v:genre\"]");
			moviebasicInfo.type = "";
			for (Element element : type) {
				moviebasicInfo.type = moviebasicInfo.type + " " + element.text();
			}
			Elements ondisplay_date = doc.select("span[property=\"v:initialReleaseDate\"]");
			moviebasicInfo.ondisplay_date = "";
			for (Element element : ondisplay_date) {
				moviebasicInfo.ondisplay_date = moviebasicInfo.ondisplay_date + " " + element.text();
			}
			moviebasicInfo.drama = doc.select("span[property=\"v:summary\"]").text();
			Elements award = doc.select("ul.award");
			moviebasicInfo.award = "";
			for (Element element : award) {
				moviebasicInfo.award = moviebasicInfo.award + " " + element.text();
			}
			moviebasicInfo.score = doc.select("strong[property=\"v:average\"]").text();
//			srdao.insertDetailedInfo(moviebasicInfo, tab, name_id);
			String reviewUrl = doc.select("div[id=\"comments-section\"]").select("span[class=\"pl\"]").select("a").attr("href");
			Document reviewdoc = this.getHtmlContent(reviewUrl);
			this.getAndSaveReview(reviewdoc, mid, "movie_comment", reviewUrl, name_id);
		}
	}
	
	private void getAndSaveReview(Document doc, String mid, String tab, String url, String name_id) {
		// TODO Auto-generated method stub
		if (doc != null) {
			this.extractReviewInfo(doc, tab, name_id);
		} else {
			System.err.println("The html is not fetched!");
		}
	}

	private void extractReviewInfo(Document doc, String tab, String name_id) {
		// TODO Auto-generated method stub
		Elements srlist = doc.select("div.comment-item");
		for(Element element : srlist){
			srInfo = new ShortCommentInfo();
			//data-cid
			srInfo.srid = element.attr("data-cid");
			srInfo.name_id = name_id;
			srInfo.reviewer = element.select("span.comment-info").select("a").text();
			srInfo.rank = element.select("span.comment-info").select("span").attr("title");
			srInfo.stars = Converter(srInfo.rank) + "";
			srInfo.date = element.select("span.comment-info").select("span[class=\"\"]").text();
			srInfo.votes = element.select("span[class=\"votes pr5\"]").text();
			srInfo.comment = element.select("p").text();
			comdao.insertShortComment(srInfo, tab);
			String reviewerUrl = element.select("span.comment-info").select("a").attr("href");
			try {
				Document reviewerdoc = this.getHtmlContent(reviewerUrl);
				this.getReviewerInfo(reviewerdoc, tab, srInfo.srid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void getReviewerInfo(Document doc, String tab, String srid) {
		srInfo = new ShortCommentInfo();
		srInfo.account = doc.select("div[class=\"user-intro\"]").select("textarea").text();
		srInfo.city = doc.select("div[class=\"user-info\"]").select("a").text();
		srInfo.sign_time = doc.select("div[class=\"user-info\"]").select("div[class=\"pl\"]").text();
		comdao.insertReviewerInfo(srInfo, tab, srid);
	}
	
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
		ShortReviewCrawler srCrawler = new ShortReviewCrawler();
		try {
			srCrawler.crawlShortReview("25723907", "movies_info");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
