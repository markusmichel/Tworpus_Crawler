/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package db;

import java.util.LinkedList;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author markus
 */
public class PersistManager {
    
    private int tweetsCount;
    private LinkedList<Tweet> tweets = new LinkedList<Tweet>();
    private final int countToSave;
    private Session session;
    
    private static PersistManager manager;
    
    public static PersistManager getInstance() {
        if(manager == null) manager = new PersistManager();
        return manager;
    }
    
    private PersistManager() {
        tweetsCount = 0;
        countToSave = 1000;
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
    }
    
    public void add(Tweet tweet) {
        tweets.add(tweet);
        tweetsCount++;
        
        //checkAndPersist();
        
        ///*
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.save(tweet);
        //session.flush();
        //session.clear();
        tx.commit();
        //*/
        
        /*
        session.save(tweet);
        if(tweetsCount > countToSave) {
            session.flush();
            tweetsCount = 0;
        }
        */
    }
    
    private void checkAndPersist() {
        if(tweetsCount < countToSave) return;
        
        manager = new PersistManager();
        
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();

                for(Tweet tweet : tweets) {
                    session.save(tweet);
                }


                session.flush();
                tx.commit();
                session.clear();
                session.close();
                tweetsCount = 0;
            }
        });
    }
}
