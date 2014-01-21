package crawler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

import twitter.OnNewStatusListener;
import twitter.StatusProcessor;
import twitter.TwitterConfig;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class StatusCrawler {
	private OnNewStatusListener onStatusListener;
	private TwitterStream twitterStream;
	private ConfigurationBuilder config;
	private CrawlerApp app;
	private BufferedWriter out;
	private int current_threads = 0;

	private HashMap<Long, Thread> processors;
	
	public StatusCrawler(CrawlerApp app) {
		app.receiveUpdateOfDebugOutput("created crawler");
		this.app = app;
		processors = new HashMap<Long, Thread>();
	}

	public void start() {
		app.receiveUpdateOfDebugOutput("running crawl-session");
		initAuth();
		initStream();
		app.receiveUpdateOfDebugOutput("Connecting to Twitter");
		twitterStream.sample();
	}

	public void stop() {
		twitterStream.shutdown();
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
	
	
	public synchronized void registerProcessor() {
		current_threads++;
	}
	
	public synchronized void unregisterProcessor(Long id) {
		if(current_threads > 0) {
			current_threads--;
		}
		processors.remove(id);
//		System.out.println(current_threads + "/" + StatusCrawlerConfig.getMaxThreads() + " threads used");
		System.gc();
	}

	private void setAuth() {
		config = new ConfigurationBuilder();
		config.setDebugEnabled(true)
				.setOAuthConsumerKey(TwitterConfig.CONSUMER_KEY)
				.setOAuthConsumerSecret(TwitterConfig.CONSUMER_SECRET)
				.setOAuthAccessToken(TwitterConfig.ACCESS_TOKEN)
				.setOAuthAccessTokenSecret(TwitterConfig.ACCESS_TOKEN_SECRET);
	}

	public void processNewStatus(Status status) {
		if(current_threads <= StatusCrawlerConfig.getMaxThreads()) {
			Long id = new Long(System.currentTimeMillis());
			processors.put(id, new Thread(new StatusProcessor(id, status, app, this)));
			processors.get(id).start();
		} else {
//			System.out.println("queue is full!");
		}
	}

}
