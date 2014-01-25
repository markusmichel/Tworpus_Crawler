package db;
// Generated 24.01.2014 10:16:50 by Hibernate Tools 3.6.0



/**
 * Tweets generated by hbm2java
 */
public class Tweet  implements java.io.Serializable {


     private long id;
     private long timestamp;
     private int timezoneoffset;
     private byte isfavourited;
     private byte isretweeted;
     private byte charcount;
     private byte wordcount;
     private int location;
     private Byte userlang;
     private Byte tweetlang;
     private float latitude;
     private float longitude;
     private Long userid;

    public Tweet() {
    }

	
    public Tweet(long id, long timestamp, int timezoneoffset, byte isfavourited, byte isretweeted, byte charcount, byte wordcount, int location, float latitude, float longitude) {
        this.id = id;
        this.timestamp = timestamp;
        this.timezoneoffset = timezoneoffset;
        this.isfavourited = isfavourited;
        this.isretweeted = isretweeted;
        this.charcount = charcount;
        this.wordcount = wordcount;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Tweet(long id, long timestamp, int timezoneoffset, byte isfavourited, byte isretweeted, byte charcount, byte wordcount, int location, Byte userlang, Byte tweetlang, float latitude, float longitude, Long userid) {
       this.id = id;
       this.timestamp = timestamp;
       this.timezoneoffset = timezoneoffset;
       this.isfavourited = isfavourited;
       this.isretweeted = isretweeted;
       this.charcount = charcount;
       this.wordcount = wordcount;
       this.location = location;
       this.userlang = userlang;
       this.tweetlang = tweetlang;
       this.latitude = latitude;
       this.longitude = longitude;
       this.userid = userid;
    }
   
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public int getTimezoneoffset() {
        return this.timezoneoffset;
    }
    
    public void setTimezoneoffset(int timezoneoffset) {
        this.timezoneoffset = timezoneoffset;
    }
    public byte getIsfavourited() {
        return this.isfavourited;
    }
    
    public void setIsfavourited(byte isfavourited) {
        this.isfavourited = isfavourited;
    }
    public byte getIsretweeted() {
        return this.isretweeted;
    }
    
    public void setIsretweeted(byte isretweeted) {
        this.isretweeted = isretweeted;
    }
    public byte getCharcount() {
        return this.charcount;
    }
    
    public void setCharcount(byte charcount) {
        this.charcount = charcount;
    }
    public byte getWordcount() {
        return this.wordcount;
    }
    
    public void setWordcount(byte wordcount) {
        this.wordcount = wordcount;
    }
    public int getLocation() {
        return this.location;
    }
    
    public void setLocation(int location) {
        this.location = location;
    }
    public Byte getUserlang() {
        return this.userlang;
    }
    
    public void setUserlang(Byte userlang) {
        this.userlang = userlang;
    }
    public Byte getTweetlang() {
        return this.tweetlang;
    }
    
    public void setTweetlang(Byte tweetlang) {
        this.tweetlang = tweetlang;
    }
    public float getLatitude() {
        return this.latitude;
    }
    
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    public float getLongitude() {
        return this.longitude;
    }
    
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
    public Long getUserid() {
        return this.userid;
    }
    
    public void setUserid(Long userid) {
        this.userid = userid;
    }




}


