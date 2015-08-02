package com.huihui.core.session;

import javax.servlet.http.HttpSession;

/**
 * Created by hadoop on 2015/7/31 0031.
 */
public interface Session {
    HttpSession getSession();

    String getId();

    Manager getManager();
    void setManager(Manager manager);

    void setNew(boolean isNew);

    boolean isValid();

    void setValid(boolean valid);

    void setCreationTime(long creationTime);
    long getCreationTime();

    void setMaxInactiveInterval(int maxInactiveInterval);
    int getMaxInactiveInterval();
    void setLastAccessedTime(long time);
    long getLastAccessedTime();
    void setId(String id);

    void expire();

}
