package com.douban.crawler;

import dao.TicketSummaryInfoDao;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MovieCrawler {

	TicketSummaryInfo ticketSummaryInfo;
	TicketSummaryInfoDao ticketSummaryInfoDao;

	public MovieCrawler() {
		// TODO Auto-generated constructor stub
	}

	//电影票房数据库，获取电影总票房，包括：年度排名、历史排名、电影名、总票房、总人次、总场次、上映日期
	//获取2000~2016年度的电影信息
	public void crawler(String tab) throws ClassNotFoundException, JSONException {
		ticketSummaryInfo = new TicketSummaryInfo();
		ticketSummaryInfoDao = new TicketSummaryInfoDao();
		for(int year=2016; year>1999; year--) {
			String url = "http://58921.com/alltime/" + year;
			Document doc = null;
			doc = this.getHtmlContent(url);
			Elements ticketlist = doc.select("div.table-responsive").select("tbody").select("tr");
			for(Element element: ticketlist) {
				String str = element.select("td").text();
				String [] strlist = str.split("\\ ");
				ticketSummaryInfo.rankYear = strlist[0];
				ticketSummaryInfo.rankHistory = strlist[1];
				ticketSummaryInfo.name = strlist[2];
				ticketSummaryInfo.ticketSummary = element.select("td").select("img").attr("src");
				ticketSummaryInfo.peopleSummary = strlist[4];
				ticketSummaryInfo.sessionSummary = strlist[5];
				ticketSummaryInfo.displayDate = strlist[6];
//				System.out.println(ticketSummaryInfo.ticketSummary);
//				System.out.println(element.getClass().getName());
//				ticketSummaryInfoDao.insertTicketSummaryInfo(ticketSummaryInfo, tab);
			}
			try {
				Thread.sleep(1*1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
				System.out.println("Error:" + e.getMessage());
			}
		}
	}

	//获取网页内容
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
			srCrawler.crawler("ticketsummary_info");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
