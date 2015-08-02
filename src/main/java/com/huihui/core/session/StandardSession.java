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
public class StandardSession implements Session, HttpSession {

    private HttpSession facade;
    private Manager manager;

    private String id;
    private boolean isNew;
    private long lastAccessedTime;
    private int maxInactiveInterval;
    private long creationTime;
    private ServletContext context;
    private Map<String, Object> attributes = new HashMap<>();
    private boolean valid;

    private boolean expiring = false;


    @Override
    public HttpSession getSession() {
        if (facade == null) {
            facade = new StandardSessionFacade(this);
        }
        return facade;
    }

    @Override
    public Manager getManager() {
        return manager;
    }

    @Override
    public void setManager(Manager manager) {
        this.manager = manager;
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
        this.valid = valid;
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
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void expire() {
        if (expiring)
            return;
        expiring = true;
        setValid(false);
        if (manager == null)
            return;
        manager.removeSession(this);
        recycle();
        expiring = false;

    }

    private void recycle() {
        // Reset the instance variables associated with this Session
        attributes.clear();
        creationTime = 0L;
        expiring = false;
        id = null;
        lastAccessedTime = 0L;
        maxInactiveInterval = -1;

        isNew = false;
        valid = false;
        Manager savedManager = manager;
        manager = null;

        // Tell our Manager that this Session has been recycled
        if ((savedManager != null) && (savedManager instanceof ManagerBase))
            ((ManagerBase) savedManager).recycle(this);
        //setAuthType(null);
        //notes.clear();
        //setPrincipal(null);
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
        // Name cannot be null
        if (name == null)
            throw new IllegalArgumentException("standardSession.setAttribute.namenull");
        // Null value is the same as removeAttribute()
        if (value == null) {
            removeAttribute(name);
            return;
        }
        if (!valid)
            return;
        attributes.put(name, value);
    }

    @Override
    public void putValue(String name, Object value) {
        setAttribute(name, value);
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
    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
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

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }
}
