package com.huihui.util;

import com.huihui.core.LifeStatus;
import com.huihui.core.Lifecycle;
import com.huihui.core.LifecycleEvent;
import com.huihui.core.LifecycleListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hadoop on 2015/7/27 0027.
 */
public final class LifeCycleSupport {

    List<LifecycleListener> listeners = Collections.synchronizedList(new ArrayList());

    Lifecycle lifecycle;

    public LifeCycleSupport(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public void addLifecycleListener(LifecycleListener listener) {
        listeners.add(listener);
    }

    public void fireLifecycleEvent(LifeStatus status,Object data){
        LifecycleEvent event = new LifecycleEvent(lifecycle,status,data);
        listeners.forEach(l->l.lifecycleEvent(event));
    }

    public void removeLifecycleListener(LifecycleListener listener) {
        listeners.remove(listener);
    }


}
