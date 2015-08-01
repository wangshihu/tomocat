package com.huihui.core.session;

import com.huihui.core.Context;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by hadoop on 2015/7/31 0031.
 */
public class StandardManager extends ManagerBase {
    private Queue<Session> recycleSession = new ConcurrentLinkedQueue<>();
    private Map<String,Session> sessionMap = new ConcurrentHashMap<>();
    private Context context;


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
        if(session==null){
            session = new StandardSession();
        }else {
            refreshSession(session);
        }
        return session;
    }

    private Session refreshSession(Session session) {

        return null;
    }




}
