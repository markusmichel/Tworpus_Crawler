package twitter;

import twitter4j.HashtagEntity;
import twitter4j.Status;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

/**
 * @deprecated Relict from last version. Just exists because of createTweet method.
 */
public class StatusProcessor implements Runnable {

    private Status status;
    
    private DBCollection collection;
    private DB db;

    public StatusProcessor(long id, DBCollection coll, DB database) {        
        collection = coll;
        db = database;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }
    
    public static BasicDBObject createTweet(Status status) {
        String lang = status.getLang();

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
        
        BasicDBObject object = new BasicDBObject()
                .append("text", status.getText())
                .append("tweetid", tweet_id)
                .append("userid", user_id)
                .append("timestamp", tweet_timestamp)
                .append("timezone_offset", tweet_timezoneoffset)
                .append("charcount", tweet_charcount)
                .append("wordcount", tweet_wordcount)
                .append("latitude", tweet_latitude)
                .append("longitude", tweet_longitude)
                .append("language", lang);
        
        BasicDBList hashtags = new BasicDBList();
        if(status.getHashtagEntities().length > 0) {
            for(HashtagEntity tag : status.getHashtagEntities())
                hashtags.add(tag.getText());
        }
        object.append("hashtags", hashtags);
        
        return object;
    }

    private BasicDBObject createTweet() {
        return createTweet(status);
    }

    @Override
    public void run() {
        if (status == null) {
            return;
        }

        BasicDBObject tweet = createTweet();
        db.requestStart();
        collection.insert(tweet);
        db.requestDone();
    }

}
