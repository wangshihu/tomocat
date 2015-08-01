package com.huihui.simple;

import com.huihui.connector.RequestWrapper;
import com.huihui.connector.ResponseWrapper;
import com.huihui.core.*;
import com.huihui.core.loader.WebappClassLoader;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

/**
 * Created by hadoop on 2015/7/25 0025.
 */
public class ServletWrapper implements Wrapper {
    private String servletClassName;
    private Servlet instance;
    private Container parent;
    private Loader loader;
    private String info;
    private String name;
    private Pipeline pipeline;

    public String getServletClassName() {
        return servletClassName;
    }

    public void setServletClassName(String servletClassName) {
        this.servletClassName = servletClassName;
        if(name==null)
            this.name = servletClassName;
    }

    @Override
    public void invoke(RequestWrapper request, ResponseWrapper response) throws IOException, ServletException {
        getPipeline().invoke(request, response);
    }

    @Override
    public Servlet allocate() throws ServletException {
        if (instance == null)
            instance = loadServletClass();
        return instance;
    }

    @Override
    public void deallocate(Servlet servlet) throws ServletException {

    }

    private Servlet loadServletClass() {
        Loader loader = getLoader();
        if (loader == null)
            throw new RuntimeException("");
        WebappClassLoader classLoader = (WebappClassLoader) loader.getClassLoader();


        if (classLoader == null)
            throw new RuntimeException("");
        Servlet servlet = null;
        try {
            servlet = (Servlet) classLoader.findClass(servletClassName).newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return servlet;
    }



    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public Pipeline getPipeline() {
        if (pipeline == null) {
            pipeline = new SimplePipeline();
            pipeline.setBasic(new SimpleWrapperValve(this));
        }
        return pipeline;
    }

    @Override
    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public Loader getLoader() {
        if (loader != null) {
            return loader;
        } else if (parent != null) {
            return parent.getLoader();
        }
        return null;
    }

    @Override
    public void setLoader(Loader loader) {

    }



    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Container getParent() {
        return parent;
    }

    @Override
    public void setParent(Container parent) {
        this.parent = parent;
    }

    @Override
    public ClassLoader getParentClassLoader() {
        return null;
    }

    @Override
    public void setParentClassLoader(ClassLoader parent) {

    }


    @Override
    public Container map(RequestWrapper request, boolean update) {
        return null;
    }

    @Override
    public void removeChild(Container child) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public Container findChild(String name) {
        throw new UnsupportedOperationException("");
    }

    @Override
    public List<Container> findChildren() {
        throw new UnsupportedOperationException("");
    }

    @Override
    public void addChild(Container child) {
        throw new UnsupportedOperationException("");
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
