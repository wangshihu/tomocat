package com.huihui.core.loader;

import com.huihui.core.Lifecycle;
import com.huihui.core.LifecycleListener;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebappClassLoader extends URLClassLoader
        implements Reloader, Lifecycle {

    public WebappClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    private List<String> repositories = new ArrayList<>();


    private Map<String, Class> loadedCache = new HashMap<>();


    private boolean start = false;


    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void start() {
        for (String respository : repositories) {
            File file = new File(respository);
            loadedResource(file, "");
        }
        start = true;
    }

    private void loadedResource(File file, String prefix) {

        for (File child : file.listFiles()) {
            if (child.isDirectory()) {
                loadedResource(child, prefix + child.getName() + "/");
            } else {
                String fileName = child.getName();
                if (fileName.endsWith(".class")) {
                    try {
                        String className = prefix.replace("/", ".") + fileName.substring(0, fileName.length() - 6);
                        Class clazz = loadClass(className);
                        loadedCache.put(className, clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }


    @Override
    public void stop() {

    }

    @Override
    public Class findClass(String name) throws ClassNotFoundException {
        Class clazz = findInnerClass(name);
        if (clazz != null) {
            return clazz;
        }
        return super.findClass(name);
    }


    public Class findInnerClass(String name) {
        return loadedCache.get(name);
    }

    @Override
    public void addRepository(String repository) {
        repositories.add(repository);
    }

    @Override
    public List<String> findRepositories() {
        return repositories;
    }

    @Override
    public boolean modified() {
        return false;
    }
}
