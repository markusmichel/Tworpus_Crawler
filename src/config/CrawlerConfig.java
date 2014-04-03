package config;

import java.util.Properties;

public class CrawlerConfig extends Properties {
	public CrawlerConfig() {
		setProperty("languages", "en,de,es,fr,it,nl,pt,tr");
		setProperty("db_host", "localhost");
		setProperty("db_port", "27017");
		setProperty("db_name", "Tworpus");
		setProperty("db_collection_name", "tweets");
	}
}
