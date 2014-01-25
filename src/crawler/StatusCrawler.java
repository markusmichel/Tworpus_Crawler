package crawler;

import java.io.BufferedWriter;
import java.io.IOException;

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
    private StatusProcessorPool processorPool;

    public StatusCrawler(CrawlerApp app) {
        app.receiveUpdateOfDebugOutput("created crawler");
        this.app = app;
        processorPool = new StatusProcessorPool(StatusCrawlerConfig.getMaxThreads());
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

    private void setAuth() {
        config = new ConfigurationBuilder();
        config.setDebugEnabled(true)
                .setOAuthConsumerKey(TwitterConfig.CONSUMER_KEY)
                .setOAuthConsumerSecret(TwitterConfig.CONSUMER_SECRET)
                .setOAuthAccessToken(TwitterConfig.ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(TwitterConfig.ACCESS_TOKEN_SECRET);
    }

    private int i = 0;
    private long start = System.currentTimeMillis();

    public void processNewStatus(Status status) {

        try {
            StatusProcessor processor = processorPool.get();
            processor.setStatus(status);
            (new Thread(processor)).start();
        } catch (Exception e) {
            System.out.println("PROCESSOR EXCEPTION:");
            e.printStackTrace();
        }

        /* 
         i++;
         long diff = System.currentTimeMillis() - start;
         long s = diff / 1000;
         if(i % 100 == 0) System.out.println("i: " + i + " in " + s + " s.");
         StatusProcessor processor = new StatusProcessor(0, status, app, this);
         processor.writeIntoDatabase();
         */
    }

}
