package com.huihui.core.session;

import com.huihui.core.context.Context;

/**
 * Created by hadoop on 2015/7/31 0031.
 */
public interface Manager {
    Context getContext();

    void setContext(Context context);

    Session createSession();

    Session findSession(String requestedSessionId);
    Session[] findSessions();
    void removeSession(Session session);
    void addSession(Session session);

    void load();

    void unload();


    void recycle(Session session);


}
