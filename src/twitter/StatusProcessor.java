package twitter;

import language.LanguageDetector;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import com.cybozu.labs.langdetect.LangDetectException;

import crawler.CrawlerApp;
import crawler.StatusCrawlerConfig;
import db.DataBase;

public class StatusProcessor implements Runnable {

	private Status status;
	private DataBase db;
	private CrawlerApp app;
	private LanguageDetector detector;
	
	public StatusProcessor(Status status, CrawlerApp app) {
		this.status = status;
		this.app = app;
		try {
			detector = new LanguageDetector();
		} catch (LangDetectException e) {
			detector = null;
		}
		db = new DataBase();
	}
	
	private void writeIntoDatabase() {
		String lang = "unknown";
		if(detector != null) {
			try {
				lang = detector.detectLanguage(status.getText());
			} catch (LangDetectException e) {
				lang = "unknown";
			}
		}
		
		if(!StatusCrawlerConfig.isInLangs(lang)) {
			System.out.println(lang);
			return;
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
		
		int tweet_isfavourited = (status.isFavorited()) ? 1 : 0;
		long tweet_isretweeted = status.getRetweetCount();
		if(tweet_isretweeted != 0) System.out.println(tweet_isretweeted);

		String text = status.getText().replaceAll("[^a-zA-Z0-9 ]+", "");
		int tweet_charcount = text.replaceAll(" ", "").length();
		int tweet_wordcount = text.split(" ").length;

		int user_location = -1;
		if (status.getUser().getLocation() != null)
			user_location = db.insertLocation(status.getUser().getLocation());

		int user_lang = -1;
		if (status.getUser().getLang() != null)
			user_lang = db.insertLang(status.getUser().getLang());
		
		int tweet_lang = -1;
			tweet_lang = db.insertLang(lang);

		if (status.getHashtagEntities().length > 0) {
			for (HashtagEntity tag : status.getHashtagEntities()) {
				int id = db.insertHashTag(tag.getText());
				if (id != -1)
					db.insertTweetToTagRelation(tweet_id, id);
			}
		}
		
		try {
		db.insertTweet(tweet_id, user_id, tweet_timestamp, tweet_timezoneoffset,
				tweet_isfavourited, tweet_isretweeted, tweet_charcount,
				tweet_wordcount, user_location, user_lang, tweet_lang, tweet_latitude,
				tweet_longitude);
		} finally {
			db.close();
		}
	}
	
	public void run() {
		writeIntoDatabase();
	}

}
