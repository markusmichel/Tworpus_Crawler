package crawler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import twitter.OnNewStatusListener;
import twitter.StatusProcessor;
import twitter.TwitterConfig;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import config.CrawlerConfig;

import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import twitter4j.FilterQuery;

public class StatusCrawler {

	private OnNewStatusListener onStatusListener;
	private TwitterStream twitterStream;
	private ConfigurationBuilder config;
	private CrawlerApp app;
	private BufferedWriter out;

	private Mongo mongoClient;
	private DB db;
	private DBCollection collection;
	
	public String name;
	
	private Properties crawlerConfig;

	public StatusCrawler(CrawlerApp app) throws UnknownHostException {
		crawlerConfig = new CrawlerConfig();
		
		app.receiveUpdateOfDebugOutput("created crawler");
		this.app = app;

		mongoClient = new Mongo(crawlerConfig.getProperty("db_host"), Integer.parseInt(crawlerConfig.getProperty("db_port")));
		db = mongoClient.getDB(crawlerConfig.getProperty("db_name"));
		collection = db.getCollection(crawlerConfig.getProperty("db_collection_name"));
	}

	public void start() {
		app.receiveUpdateOfDebugOutput("running crawl-session");
		initAuth();
		initStream();
		app.receiveUpdateOfDebugOutput("Connecting to Twitter");
		
		String[] language = crawlerConfig.getProperty("languages").split(",");
		FilterQuery fq = new FilterQuery();
		 fq.language(language);
		twitterStream.filter(fq);
//		twitterStream.sample();
	}

	public void stop() {
		System.out.println("Shutdown twitter stream");
		twitterStream.shutdown();
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initAuth() {
		app.receiveUpdateOfDebugOutput("initAuth() in crawler");
		setAuth();
	}

	private void initStream() {
		app.receiveUpdateOfDebugOutput("initStream() in crawler");
		onStatusListener = new OnNewStatusListener(this);
		twitterStream = null;
		TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(
				config.build());
		twitterStream = twitterStreamFactory.getInstance();
		twitterStream.addListener(onStatusListener);
	}

	private void setAuth() {
		config = new ConfigurationBuilder();
		config.setDebugEnabled(true)
				.setOAuthConsumerKey(TwitterConfig.CONSUMER_KEY)
				.setOAuthConsumerSecret(TwitterConfig.CONSUMER_SECRET)
				.setOAuthAccessToken(TwitterConfig.ACCESS_TOKEN)
				.setOAuthAccessTokenSecret(TwitterConfig.ACCESS_TOKEN_SECRET);
	}

	private int i = 0;
	private final long start = System.currentTimeMillis();
	float sec, lastSec;
	List<DBObject> statuses = new LinkedList<DBObject>();

	public void processNewStatus(Status status) {
		i++;
		sec = (System.currentTimeMillis() - start) / 1000f;

		if (sec - lastSec >= 1) {
			System.out.println(i + " tweets after " + sec + " in " + name);
			lastSec = sec;
		}
		
		BasicDBObject object = StatusProcessor.createTweet(status);

		if (object != null)
			collection.save(object);
	}

	public void deleteStatus(StatusDeletionNotice deletion) {
		DBObject obj = new BasicDBObject();
		obj.put("tweetid", deletion.getStatusId());
		obj.put("userid", deletion.getUserId());
		
		collection.remove(obj);
		
	}

}
