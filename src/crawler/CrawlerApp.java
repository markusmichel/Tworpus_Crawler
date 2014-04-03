package crawler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CrawlerApp {

	private static CrawlerApp app;
	private StatusCrawler crawler;

	public static void main(String[] args) throws IOException {
		
		startCrawler();
	}

	public static void startCrawler() {
		app = new CrawlerApp();
		app.crawlTweets();
	}

	public CrawlerApp() {
	}

	public void crawlTweets() {
		System.out.println("START CRAWLING");
		final CrawlerApp that = this;
		
		for (int i = 0; i < 1; i++) {
			System.out.println("init new crawler");
			final String name = "Crawler " + i;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						StatusCrawler crawler = new StatusCrawler(that);
						crawler.name = name;
						crawler.start();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				}
			}).start();;
		}
	}

	public void receiveUpdateOfDebugOutput(String msg) {
		System.out.println(msg);
	}

	public void stop() {
		System.out.println("stop");
		crawler.stop();
	}

}
