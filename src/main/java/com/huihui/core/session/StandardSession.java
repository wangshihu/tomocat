package com.huihui.core.session;

import com.huihui.util.Enumerator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hadoop on 2015/7/31 0031.
 */
public class StandardSession implements Session,HttpSession {

    private HttpSession facade;
    private Mananager mananager;

    private String id;
    private boolean isNew;
    private long lastAccessedTime;
    private int maxInactiveInterval;
    private long creationTime;
    private ServletContext context;
    private Map<String,Object> attributes= new HashMap<>();
    private boolean valid;


    @Override
    public HttpSession getSession() {
        if(facade==null){
            facade = new StandardSessionFacade(this);
        }
        return facade;
    }

    @Override
    public Mananager getManager() {
        return mananager;
    }

    @Override
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void setValid(boolean valid) {
        this.valid =valid;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    @Override
    public ServletContext getServletContext() {
        return context;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        this.maxInactiveInterval = interval;
    }

    @Override
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }


    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }


    @Override
    public Enumeration<String> getAttributeNames() {
        return new Enumerator(attributes.keySet());
    }



    @Override
    public void setAttribute(String name, Object value) {
        if(attributes.get(name)==null)
            return;
        attributes.put(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        attributes.put(name,value);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }
    @Override
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public void invalidate() {

    }





    @Override
    public void removeValue(String name) {

    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public Object getValue(String name) {
        return null;
    }
    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }


}
