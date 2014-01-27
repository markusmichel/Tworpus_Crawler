package twitter;

import language.LanguageDetector;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import com.cybozu.labs.langdetect.LangDetectException;

import crawler.StatusCrawlerConfig;
import crawler.StatusProcessorPool;
import db.HibernateUtil;
import db.Tweet;
import org.hibernate.Session;
import org.hibernate.Transaction;
import twitter4j.json.DataObjectFactory;

public class StatusProcessor implements Runnable {

    private Status status;
    private int count;
    private LanguageDetector detector;
    private final long id;
    private final StatusProcessorPool pool;
    private Session session;
    private Transaction transaction;

    public StatusProcessor(long id, StatusProcessorPool pool) {
        this.pool = pool;
        this.id = id;
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        
        try {
            detector = new LanguageDetector();
        } catch (LangDetectException e) {
            detector = null;
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }
    
    /**
     * Return the number of items in the processor's session
     * which aren't yet persisted to the database.
     */
    public int getCount() {
        return count;
    }
    
    /**
     * Creates a hibernate compatible Tweet object.
     */
    private Tweet createTweet() {
        String lang = "unknown";
        if (detector != null) {
            try {
                lang = detector.detectLanguage(status.getText());
            } catch (LangDetectException e) {
                lang = "unknown";
            }
        }
        
        String json = DataObjectFactory.getRawJSON(status);
        String langCodeTmp = status.getIsoLanguageCode();
        String textTmp = status.getText();
        if (!StatusCrawlerConfig.isInLangs(lang)) {
            return null;
        }

        long tweet_id = status.getId();
        long user_id = status.getUser().getId();

        double tweet_latitude = -1;
        double tweet_longitude = -1;
        if (status.getGeoLocation() != null) {
            tweet_latitude = status.getGeoLocation().getLatitude();
            tweet_longitude = status.getGeoLocation().getLongitude();
        }

        long tweet_timestamp = status.getCreatedAt().getTime();
        int tweet_timezoneoffset = status.getUser().getUtcOffset() / 3600;

        String text = status.getText().replaceAll("[^a-zA-Z0-9 ]+", "");
        int tweet_charcount = text.length();
        int tweet_wordcount = text.split(" ").length;

        Tweet tweet = new Tweet();
        tweet.setId(tweet_id);
        tweet.setUserid(user_id);
        tweet.setTimestamp(tweet_timestamp);
        tweet.setTimezoneoffset(tweet_timezoneoffset);
        tweet.setCharcount((byte) tweet_charcount); // @TODO check loss of precision
        tweet.setWordcount((byte) tweet_wordcount);
        tweet.setLatitude((float) tweet_latitude);
        tweet.setLongitude((float) tweet_longitude);

        //System.out.println("lang: " + status.getUser().getLang());
        
        return tweet;

        /*
         if (status.getHashtagEntities().length > 0) {
         for (HashtagEntity tag : status.getHashtagEntities()) {
                            
         int id = db.insertHashTag(tag.getText());
         if (id != -1)
         db.insertTweetToTagRelation(tweet_id, id);
         }
         }
         */
        /*
         db.insertTweetAndCloseDatabase(tweet_id, user_id, tweet_timestamp, tweet_timezoneoffset, tweet_charcount,
         tweet_wordcount, user_location, user_lang, tweet_lang, tweet_latitude,
         tweet_longitude);
         */
    }
    
    private void save(Tweet tweet) {
        if(tweet == null) return;
        count++;
        try {
            session.save(tweet);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("count = " + count + " id=" + id);
        if(count >= 100) {
            System.out.println("save at count = " + count + " id=" + id);
            count = 0;
            session.flush();
            session.clear();
        }
    }
 
    @Override
    public void run() {
        if (status == null) return;
        
        Tweet tweet = createTweet();
        save(tweet);
        finish();
    }

    public void finish() {
        System.out.println("Push processor back to pool: " + id);
        status = null;
        pool.setProcessAsleep(this);
    }

}
