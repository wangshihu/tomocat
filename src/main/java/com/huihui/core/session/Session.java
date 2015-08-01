package com.huihui.core.session;

import javax.servlet.http.HttpSession;

/**
 * Created by hadoop on 2015/7/31 0031.
 */
public interface Session {
    HttpSession getSession();

    String getId();

    Mananager getManager();

    void setNew(boolean isNew);

    boolean isValid();

    void setValid(boolean valid);

}
