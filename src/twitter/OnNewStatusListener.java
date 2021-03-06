package twitter;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import crawler.StatusCrawler;

public class OnNewStatusListener implements StatusListener {
	
	private StatusCrawler crawler;
	
	public OnNewStatusListener(StatusCrawler crawler) {
		this.crawler = crawler;
		
	}

	@Override
	public void onStatus(Status status) {
		crawler.processNewStatus(status);
	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
	}

	@Override
	public void onException(Exception arg0) {
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice deletion) {
		crawler.deleteStatus(deletion);
	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {

	}

	@Override
	public void onStallWarning(StallWarning warning) {		
	}
}
