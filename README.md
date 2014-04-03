Tworpus Crawler
===============

Console program, which constantly fetches tweets from the Twitter Streaming API and saved them to a Mongo Database.
The crawler only saves metadata - no content!

## requires
- twitter4j fork: https://github.com/markusmichel/twitter4j
- mongo-java-driver (tested with 2.9.3)

## config
MongoDB is excepted to run on `localhost:27017`.
Database is named `Tworpus` and collection for saved tweet ids is named `tweets`.

The default languages to crawl are 
- en
- de
- es
- fr
- it
- nl
- pt
- tr

All values can be edited in [config/CrawlerConfig.java]("https://github.com/markusmichel/Tworpus_Crawler/blob/master/src/config/CrawlerConfig.java")
