package com.huihui.simple;

import com.huihui.core.Container;
import com.huihui.core.LifecycleListener;
import com.huihui.core.Loader;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 2015/7/25 0025.
 */
public class HostLoader implements Loader {
    public static final String LIB_ROOT =
            System.getProperty("user.dir") + File.separator  + "lib";
    private ClassLoader classLoader;
    private Container container;
    @Override
    public ClassLoader getClassLoader() {
        if(classLoader==null)
            classLoader = initClassLoader();
        return classLoader;
    }

    private ClassLoader initClassLoader() {
        File file = new File(LIB_ROOT);
        List<URL> urlList = new ArrayList<>();
        try {
            for(File lib:file.listFiles()){
                urlList.add(new URL("file:"+lib.getCanonicalPath()));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new URLClassLoader(urlList.toArray(new URL[0]),Thread.currentThread().getContextClassLoader());
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public boolean getDelegate() {
        return false;
    }

    @Override
    public void setDelegate(boolean delegate) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public boolean isReloadable() {
        return false;
    }


    @Override
    public void setReloadable(boolean reloadable) {

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }


    @Override
    public boolean modified() {
        return false;
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
