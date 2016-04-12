package com.douban.crawler;

import dao.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MovieCrawler {

	TopTicketsInfo topTicketsInfo;
	TicketsHistoryInfo ticketsHistoryInfo;
	TicketsMonthInfo ticketsMonthInfo;
	TicketsWeekInfo ticketsWeekInfo;
	SessionInfo sessionInfo;
	SessionTrendInfo sessionTrendInfo;
	TopTicketsInfoDao topTicketsInfoDao;
	TicketsHistoryInfoDao ticketsHistoryInfoDao;
	TicketsMonthInfoDao ticketsMonthInfoDao;
	TicketsWeekInfoDao ticketsWeekInfoDao;
	SessionInfoDao sessionInfoDao;
	SessionTrendInfoDao sessionTrendInfoDao;

	public MovieCrawler() {
		// TODO Auto-generated constructor stub
	}

	//CBO中国票房首页CBO实时票房榜
	public void crawler(String tab) throws ClassNotFoundException, JSONException {
//		topTicketsInfoDao = new TopTicketsInfoDao();
//		topTicketsInfo = new TopTicketsInfo();
//		String url = "http://www.cbooo.cn/";
//		Document doc = null;
//		doc = this.getHtmlContent(url);
//		Elements topmovielist = doc.select("div.banner_left").select("div[id=\"top_list\"]").select("tbody").select("tr");
//		for (Element element: topmovielist) {
//			String str = element.select("td").text();
//			String [] strlist = str.split("\\ ");
//			topTicketsInfo.id = strlist[0];
//			topTicketsInfo.name = strlist[1];
//			topTicketsInfo.real_time_tickets = strlist[2];
//			topTicketsInfo.ticket_percent = strlist[3];
//			topTicketsInfo.sum_tickets = strlist[4];
//			topTicketsInfo.display_percent = strlist[5];
//			topTicketsInfo.days = strlist[6];
//			topTicketsInfoDao.insertTopTicketsInfo(topTicketsInfo, tab, topTicketsInfo.id);
//			try {
//				Thread.sleep(3*1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		getTicketsHistory("tickets_history");
//		getTicketsMonth("tickets_month");
		getTicketsWeek("tickets_week");
//		getSessionInfo("session_info");
//		getSessionTrendInfo("session_trend_info");
	}

	//历史票房页面获取电影排名、电影名、类型、年份、总票房、平均票价、平均人次、地区、日期
	public void getTicketsHistory(String tab) throws ClassNotFoundException {
		ticketsHistoryInfoDao = new TicketsHistoryInfoDao();
		ticketsHistoryInfo = new TicketsHistoryInfo();
		for(int year=2016; year>2008; year--) {
			String url = "http://www.cbooo.cn/year?year=" + year;
			Document doc = null;
			doc = this.getHtmlContent(url);
			Elements tickethistorylist = doc.select("div[class=\"tabbox tabbox02\"]").select("table[class=\"date date01\"]").select("tbody").select("tr");
			for(Element element: tickethistorylist) {
				String str = element.select("td").text();
				String [] strlist = str.split("\\ ");
				if(strlist.length == 7) {
					ticketsHistoryInfo.name = strlist[0];
					ticketsHistoryInfo.type = strlist[1];
					ticketsHistoryInfo.year = year;
					ticketsHistoryInfo.sum_tickets = strlist[2];
					ticketsHistoryInfo.price_ave = strlist[3];
					ticketsHistoryInfo.people_ave = strlist[4];
					ticketsHistoryInfo.area = strlist[5];
					ticketsHistoryInfo.date = strlist[6];
					ticketsHistoryInfoDao.insertTicketsHistoryInfo(ticketsHistoryInfo, tab);
				}
			}
			try {
				Thread.sleep(3*1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
				System.out.println("Error:" + e.getMessage());
			}
		}
	}

	//获取电影的实时排片信息
	public void getSessionInfo(String tab) throws ClassNotFoundException, JSONException {
		sessionInfo = new SessionInfo();
		sessionInfoDao = new SessionInfoDao();
		String url = "http://www.cbooo.cn/Screen/getScreenData?days=0";
		Document doc = null;
		doc = this.getHtmlContent(url);
		String html = doc.select("body").text();
		JSONObject obj = new JSONObject(html);
		JSONArray jsonArray1 = (JSONArray) obj.get("data1");
		//从url返回的第一组json数据中得到电影的id、电影名、全国总场次、全国票房占比
		for(int i=0;i<jsonArray1.length();i++) {
			JSONObject jsonObject = jsonArray1.getJSONObject(i);
			sessionInfo.movie_id = Integer.parseInt(jsonObject.getString("movieid"));
			sessionInfo.name = jsonObject.getString("moviename");
			sessionInfo.movie_num = Integer.parseInt(jsonObject.getString("movienum"));
			sessionInfo.per_num = jsonObject.getString("Per_num");
//			sessionInfoDao.insertBasicInfo(sessionInfo, tab);
		}
		JSONArray jsonArray2 = (JSONArray) obj.get("data4");
		//从url返回的第四组json数据中得到电影的认知指数
		for(int i=0;i<jsonArray2.length();i++) {
			JSONObject jsonObject = jsonArray2.getJSONObject(i);
			int movieid = Integer.parseInt(jsonObject.getString("MovieID"));
			sessionInfo.renzhi_index = jsonObject.getString("RenZhiIndex");
//			sessionInfoDao.updateRenZhiIndex(sessionInfo, tab, movieid);
		}
		JSONArray jsonArray3 = (JSONArray) obj.get("data5");
		//从url返回的第五组json数据中得到电影的购票指数
		for(int i=0;i<jsonArray3.length();i++) {
			JSONObject jsonObject = jsonArray3.getJSONObject(i);
			int movieid = Integer.parseInt(jsonObject.getString("MovieID"));
			sessionInfo.tickets_index = jsonObject.getString("BuyTicketIndex");
//			sessionInfoDao.updateTicketIndex(sessionInfo, tab, movieid);
		}
		JSONArray jsonArray4 = (JSONArray) obj.get("data6");
		//从url返回的第六组json数据中得到电影的口碑指数
		for(int i=0;i<jsonArray4.length();i++) {
			JSONObject jsonObject = jsonArray4.getJSONObject(i);
			int movieid = Integer.parseInt(jsonObject.getString("MovieID"));
			sessionInfo.rap_index = jsonObject.getString("RapIndex");
			sessionInfoDao.updateRapIndex(sessionInfo, tab, movieid);
		}
	}

	//获取电影实时票房趋势，包括电影的id，电影名，日期以及日期对应的票房信息
	public void getSessionTrendInfo(String tab) throws ClassNotFoundException, JSONException {
		sessionTrendInfo = new SessionTrendInfo();
		sessionTrendInfoDao = new SessionTrendInfoDao();
		String url = "http://www.cbooo.cn/Screen/getTrendScreenData";
		Document doc = null;
		doc = this.getHtmlContent(url);
		String html = doc.select("body").text();
		JSONObject obj = new JSONObject(html);
		JSONArray jsonArray1 = (JSONArray) obj.get("data1");
		for(int i=0;i<jsonArray1.length();i++) {
			JSONObject jsonObject =  jsonArray1.getJSONObject(i);
			sessionTrendInfo.movie_id = 0;
			sessionTrendInfo.name = "全国";
			sessionTrendInfo.date = jsonObject.getString("sdate");
			sessionTrendInfo.m_daysum = Integer.parseInt(jsonObject.getString("num"));
//			sessionTrendInfoDao.updateNationalNum(sessionTrendInfo, tab);
		}
		JSONArray jsonArray2 = (JSONArray) obj.get("data2");
		for(int i=0;i<jsonArray2.length();i++) {
			JSONObject jsonObject = jsonArray2.getJSONObject(i);
			sessionTrendInfo.movie_id = Integer.parseInt(jsonObject.getString("m_id"));
			sessionTrendInfo.name = jsonObject.getString("m_name");
			sessionTrendInfo.date = jsonObject.getString("m_date");
			sessionTrendInfo.m_daysum = Integer.parseInt(jsonObject.getString("m_daysum"));
			sessionTrendInfoDao.updateMovieNum(sessionTrendInfo, tab);
		}
	}

	//获取单月票房数据，包含单月排名、id、电影名、月内天数、单月票房、平均票价、场均人次、月度占比、上映时间、口碑指数、月份
	public void getTicketsMonth(String tab) throws ClassNotFoundException, JSONException {
		ticketsMonthInfo = new TicketsMonthInfo();
		ticketsMonthInfoDao = new TicketsMonthInfoDao();
		String url = "http://www.cbooo.cn/boxOffice/getMonthBox?sdate=2016-01-01";
		Document doc = null;
		doc = this.getHtmlContent(url);
		String html = doc.select("body").text();
		JSONObject obj = new JSONObject(html);
		JSONArray jsonArray = (JSONArray) obj.get("data1");
		for(int i=0;i<jsonArray.length();i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ticketsMonthInfo.rank = Integer.parseInt(jsonObject.getString("Irank"));
			if(ticketsMonthInfo.rank<11) {
				ticketsMonthInfo.movie_id = Integer.parseInt(jsonObject.getString("EnMovieID"));
				ticketsMonthInfo.name = jsonObject.getString("MovieName");
				ticketsMonthInfo.days = Integer.parseInt(jsonObject.getString("days"));
				ticketsMonthInfo.tickets_month = Integer.parseInt(jsonObject.getString("boxoffice"));
				ticketsMonthInfo.price_ave = Integer.parseInt(jsonObject.getString("avgboxoffice"));
				ticketsMonthInfo.people_ave = Integer.parseInt(jsonObject.getString("avgshowcount"));
				ticketsMonthInfo.per_month = jsonObject.getString("box_pro");
				ticketsMonthInfo.release_time = jsonObject.getString("releaseTime");
				ticketsMonthInfo.wom_index = jsonObject.getString("WomIndex");
				ticketsMonthInfo.date = "2016-01";
				System.out.println(ticketsMonthInfo.name);
				ticketsMonthInfoDao.insertTicketsMonthInfo(ticketsMonthInfo, tab);
				try {
					Thread.sleep(1*1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	//获取单周票房数据，包含：电影排名、id、电影名、单周票房、电影总票贩个、上映日期、平均票价、场均人次、口碑指数
	public void getTicketsWeek(String tab) throws ClassNotFoundException, JSONException {
		ticketsWeekInfo = new TicketsWeekInfo();
		ticketsWeekInfoDao = new TicketsWeekInfoDao();
		String url = "http://www.cbooo.cn/boxOffice/getWeekInfoData?sdate=2016-02-15";
		Document doc = null;
		doc = this.getHtmlContent(url);
		String html = doc.select("body").text();
		JSONObject obj = new JSONObject(html);
		JSONArray jsonArray = (JSONArray) obj.get("data1");
		for(int i=0;i<jsonArray.length();i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			ticketsWeekInfo.rank = Integer.parseInt(jsonObject.getString("MovieRank"));
			ticketsWeekInfo.movie_id = Integer.parseInt(jsonObject.getString("MovieID"));
			ticketsWeekInfo.name = jsonObject.getString("MovieName");
			ticketsWeekInfo.tickets_week = Integer.parseInt(jsonObject.getString("WeekAmount"));
			ticketsWeekInfo.tickets_sum = Integer.parseInt(jsonObject.getString("SumWeekAmount"));
			ticketsWeekInfo.days = Integer.parseInt(jsonObject.getString("MovieDay"));
			ticketsWeekInfo.price_ave = Integer.parseInt(jsonObject.getString("AvgPrice"));
			ticketsWeekInfo.people_ave = Integer.parseInt(jsonObject.getString("AvgPeople"));
			ticketsWeekInfo.wom_index = jsonObject.getString("WomIndex");
			ticketsWeekInfoDao.insertTicketsWeekInfo(ticketsWeekInfo, tab);
			try {
				Thread.sleep(1*1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
			srCrawler.crawler("movie_tickets");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
