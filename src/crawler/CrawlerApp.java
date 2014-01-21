package crawler;

import java.io.IOException;

import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import db.DataBase;


public class CrawlerApp {
	
	private static CrawlerApp app;
	private StatusCrawler crawler;
	
	public static void main(String[] args) throws LangDetectException, IOException {
		try {
			StatusCrawlerConfig.loadConfig("crawler_config.cfg");
		} catch (InvalidConfigException e) {
			System.exit(100);
		}

		
		try{
			startCrawler();
		} catch(Exception e) {
			startCrawler();
		}
	}
	
	public static void startCrawler() {
		try {
			StatusCrawlerConfig.loadConfig("crawler_config.cfg");
		} catch (InvalidConfigException e) {
			System.exit(100);
		}

		
		try {
			DetectorFactory.loadProfile(StatusCrawlerConfig.getPROFILE_DIR());
		} catch (LangDetectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataBase.initDataBase(StatusCrawlerConfig.getDB_HOST(), StatusCrawlerConfig.getDB_PORT(), StatusCrawlerConfig.getDB_DATABASE_NAME(), StatusCrawlerConfig.getDB_USER(), StatusCrawlerConfig.getDB_PASSWORD());
		app = new CrawlerApp();
		app.crawlTweets();
	}
	
	public CrawlerApp() {
	}
	
	public void crawlTweets() {
		crawler = new StatusCrawler(this);
		crawler.start();
		
	}
	
	public void receiveUpdateOfDebugOutput(String msg) {
		System.out.println(msg);
	}

	
	public void stop() {
		System.out.println("stop");
		crawler.stop();
	}
	
	
}
