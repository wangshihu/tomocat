package com.huihui.simple;


import com.huihui.core.Lifecycle;
import com.huihui.core.LifecycleEvent;
import com.huihui.core.LifecycleListener;

public class SimpleContextLifecycleListener implements LifecycleListener {

  public void lifecycleEvent(LifecycleEvent event) {
    Lifecycle lifecycle = event.getLifecycle();
    System.out.println("SimpleContextLifecycleListener's event " +event.getStatus());
  }
}