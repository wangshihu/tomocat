package com.huihui.core.session;

import com.huihui.core.Context;

/**
 * Created by hadoop on 2015/7/31 0031.
 */
public interface Mananager {
    Context getContext();

    void setContext(Context context);

    Session createSession();



}
