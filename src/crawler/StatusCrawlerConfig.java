package crawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;

public class StatusCrawlerConfig {

	
	private static String DB_HOST;
	private static String DB_PORT;
	private static String DB_USER;
	private static String DB_PASSWORD;
	private static String DB_DATABASE_NAME;
	private static String PROFILE_DIR;
	private static int MAX_THREADS = 60;
	private static String[] LANGS;
	
	
	
	public static boolean loadConfig(String configFile) throws InvalidConfigException {
		HashMap<String, String> configValues = new HashMap<String, String>();
		FileInputStream inputStream;
		String line;
		BufferedReader reader;
		try {
			inputStream = new FileInputStream(new File(configFile));
			reader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = reader.readLine()) != null) {
				putConfigLineInMap(line, configValues);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			throw new InvalidConfigException();
		} catch (IOException e) {
			throw new InvalidConfigException();
		}
		if(configValues.size() == 0) {
			throw new InvalidConfigException();
		} else {
			DB_HOST = configValues.get("DB_HOST");
			DB_PORT = configValues.get("DB_PORT");
			DB_USER = configValues.get("DB_USER");
			DB_PASSWORD = configValues.get("DB_PASSWORD");
			DB_DATABASE_NAME = configValues.get("DB_DATABASE_NAME");
			PROFILE_DIR = configValues.get("PROFILE_DIR").replace("/", File.separator);
			MAX_THREADS = Integer.valueOf(configValues.get("MAX_THREADS"));
			LANGS = configValues.get("LANGS").split(",");
			if(DB_HOST!=null && DB_PORT!=null && DB_USER!=null && DB_PASSWORD!= null && DB_DATABASE_NAME!=null && DB_DATABASE_NAME!=PROFILE_DIR) {
				return true;
			} else throw new InvalidConfigException();
		}
	}
	
	private static String putConfigLineInMap(String line, HashMap<String, String> map) {
		String[] keyAndValue = line.split(":");
		if(keyAndValue.length == 2) {
			return map.put(keyAndValue[0], keyAndValue[1]);
		}
		return null;
	}

	public static String getDB_HOST() {
		return DB_HOST;
	}

	public static String getDB_PORT() {
		return DB_PORT;
	}

	public static String getDB_USER() {
		return DB_USER;
	}

	public static String getDB_PASSWORD() {
		return DB_PASSWORD;
	}

	public static String getDB_DATABASE_NAME() {
		return DB_DATABASE_NAME;
	}
	
	public static String getPROFILE_DIR() {
		return PROFILE_DIR;
	}
	
	public static int getMaxThreads() {
		return MAX_THREADS;
	}
	
	public static boolean isInLangs(String lang) {
		return Arrays.asList(LANGS).contains(lang);
	}
}

