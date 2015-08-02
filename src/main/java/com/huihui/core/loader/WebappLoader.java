package com.huihui.core.loader;

import com.huihui.core.*;
import com.huihui.core.context.Context;
import com.huihui.core.io.ContextDirFile;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WebappLoader
        implements Lifecycle, Loader, Runnable {

    private WebappClassLoader classLoader;
    private Container container;
    private static String info = WebappLoader.class.getName();
    private List<String> repositories = new ArrayList<>();


    private boolean reloadable = false;

    public WebappLoader(Container container) {
        this.container = container;
    }

    @Override
    public ClassLoader getClassLoader() {
        return classLoader;
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
        return info;
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
        this.classLoader = createClassLoader();
        classLoader.start();

    }

    private WebappClassLoader createClassLoader() {
        ContextDirFile dirFile = ((Context) container).getDirFile();
        File WEB_INFO = new File(dirFile.getSource().getAbsoluteFile()+File.separator+"WEB-INF");
        File libFile = new File(WEB_INFO.getAbsoluteFile()+File.separator+"lib");
        if(!libFile.exists())
            libFile.mkdirs();
        File classFile = new File(WEB_INFO.getAbsoluteFile()+File.separator+"classes");
        if(!classFile.exists())
            classFile.mkdirs();
        List<URL> urlList = new ArrayList<>();
        try {
            for(File lib:libFile.listFiles()){
                urlList.add(new URL("file:"+lib.getCanonicalPath()));
            }
            urlList.add(new URL("file:"+classFile.getCanonicalPath()+File.separator));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       WebappClassLoader classLoader = new WebappClassLoader(urlList.toArray(new URL[0]),container.getParentClassLoader());
        classLoader.addRepository(classFile.getAbsolutePath());
        return classLoader;
    }

    @Override
    public void stop() {

    }

    @Override
    public void run() {

    }
    @Override
    public boolean isReloadable() {
        return reloadable;
    }

    @Override
    public void setReloadable(boolean reloadable) {
        this.reloadable = reloadable;
    }
}