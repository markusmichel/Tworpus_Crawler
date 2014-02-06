package crawler;

import java.io.IOException;

public class CrawlerApp {
	
	private static CrawlerApp app;
	private StatusCrawler crawler;
	
	public static void main(String[] args) throws IOException {
            ///*
            try {
                StatusCrawlerConfig.loadConfig("crawler_config.cfg");
            } catch (InvalidConfigException e) {
                System.exit(100);
            }

            try {
                startCrawler();
            } catch (Exception e) {
                startCrawler();
            }
            //*/
            
            /*
            for(int i=0; i<12; i++) {
                Tweet tweet = new Tweet();
                tweet.setTimestamp(000000);
                tweet.setCharcount((byte)i);
                tweet.setWordcount((byte)i);
                PersistManager.getInstance().add(tweet);
            }
            */
	}
	
	public static void startCrawler() {
		try {
			StatusCrawlerConfig.loadConfig("crawler_config.cfg");
		} catch (InvalidConfigException e) {
			System.exit(100);
		}
		
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
