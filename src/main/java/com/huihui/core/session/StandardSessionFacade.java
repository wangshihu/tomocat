package com.huihui.core.session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * Created by hadoop on 2015/7/31 0031.
 */
public class StandardSessionFacade implements HttpSession {
    private StandardSession standardSession;
    public StandardSessionFacade(StandardSession standardSession) {
        this.standardSession = standardSession;
    }

    @Override
    public long getCreationTime() {
        return standardSession.getCreationTime();
    }

    @Override
    public String getId() {
        return standardSession.getId();
    }

    @Override
    public long getLastAccessedTime() {
        return standardSession.getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() {
        return standardSession.getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        standardSession.setMaxInactiveInterval(interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        return standardSession.getMaxInactiveInterval();
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return standardSession.getSessionContext();
    }

    @Override
    public Object getAttribute(String name) {
        return standardSession.getAttribute(name);
    }

    @Override
    public Object getValue(String name) {
        return standardSession.getValue(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return standardSession.getAttributeNames();
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String name, Object value) {
        standardSession.setAttribute(name,value);
    }

    @Override
    public void putValue(String name, Object value) {
        standardSession.putValue(name,value);
    }

    @Override
    public void removeAttribute(String name) {
        standardSession.removeAttribute(name);
    }

    @Override
    public void removeValue(String name) {
        standardSession.removeValue(name);
    }

    @Override
    public void invalidate() {
        standardSession.invalidate();
    }

    @Override
    public boolean isNew() {
        return standardSession.isNew();
    }
}
