package com.douban.crawler;

public class MyThread extends Thread {
	
	public MyThread(String tab) {
		run(tab);
	}
	
	public void run(String tab) {
		MovieCrawler srCrawler = new MovieCrawler();
		try {
			srCrawler.crawler(tab);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
