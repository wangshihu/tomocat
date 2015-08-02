package com.huihui.core.session;

import com.huihui.core.Lifecycle;
import com.huihui.core.LifecycleListener;
import com.huihui.core.context.Context;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by hadoop on 2015/7/31 0031.
 */
public class StandardManager extends ManagerBase implements Lifecycle,Runnable{
    private Queue<Session> recycleSession = new ConcurrentLinkedQueue<>();
    private Map<String,Session> sessionMap = new ConcurrentHashMap<>();
    private Context context;
    private int maxInactiveInterval =60;
    //检查Session的暂停时间
    private int checkTime = 60;



    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void setContext(Context context) {
        this.context =context;
    }

    @Override
    public Session createSession() {
        Session session = null;
        if(!recycleSession.isEmpty() ){
            session = recycleSession.poll();
        }
        if(session==null)
            session = new StandardSession();
        refreshSession(session);
        return session;
    }

    @Override
    public Session findSession(String requestedSessionId) {

        return sessionMap.get(requestedSessionId);
    }



    private void refreshSession(Session session) {
        long nowTime = System.currentTimeMillis();
        session.setCreationTime(nowTime);
        session.setLastAccessedTime(nowTime);
        session.setManager(this);

        session.setMaxInactiveInterval(this.maxInactiveInterval);//设置过期秒

        session.setId(generateSessionId());//设置id
        session.setNew(true);
        session.setValid(true);
        addSession(session);
    }

    @Override
    public void start() {
        load();
    }

    @Override
    public void stop() {
        unload();
    }

    @Override
    public void run() {
        while(Thread.interrupted()){
            try {
                TimeUnit.SECONDS.sleep(checkTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkSessionExpire();
        }
    }

    private void checkSessionExpire() {
        Session[] sessions = findSessions();
        for(Session session:sessions){
            int maxInactiveInterval = session.getMaxInactiveInterval();
            if(!session.isValid()||maxInactiveInterval<0)
                continue;
            long timeTidle = (System.currentTimeMillis()- session.getLastAccessedTime())/1000L;
            if(timeTidle>maxInactiveInterval)
                session.expire();
        }
    }

    /**
     *获得当前的session集合，防止在多线程下变化。
     * @return
     */
    @Override
    public Session[] findSessions() {
        synchronized (sessionMap){
           return sessionMap.values().toArray(new Session[0]);
        }

    }

    @Override
    public void removeSession(Session session) {
        if(session==null||session.getId()==null)
            throw new IllegalArgumentException("session connot be null or session id connot be null");
        sessionMap.remove(session.getId());
    }

    @Override
    public void addSession(Session session) {
        if(session==null||session.getId()==null)
            throw new IllegalArgumentException("session connot be null or session id connot be null");
        sessionMap.put(session.getId(),session);
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Override
    public void recycle(Session session) {
        recycleSession.add(session);
    }


    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }


}
