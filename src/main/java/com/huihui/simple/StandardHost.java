package com.huihui.simple;

import com.huihui.connector.HttpRequest;
import com.huihui.connector.HttpResponse;
import com.huihui.core.*;
import com.huihui.core.io.ContextDirFile;
import com.huihui.valves.StandardHostValve;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.*;

/**
 * Created by hadoop on 2015/7/28 0028.
 */
public class StandardHost implements Host {

    private Map<String, Context> contextRegistry = new HashMap<>();
    private String info;
    private Pipeline pipeline;
    private String name;
    private Container parent;

    private List<Container> children = Collections.synchronizedList(new ArrayList());
    private boolean started = false;
    private Loader loader;


    @Override
    public Context getContext(String uri) {
        return contextRegistry.get(uri);
    }

    @Override
    public URLClassLoader getRootClassLoader() {
        return null;
    }


    @Override
    public ClassLoader getParentClassLoader() {
        return null;
    }

    @Override
    public void setParentClassLoader(ClassLoader parent) {

    }

    @Override
    public void addChild(Container child) {
        children.add(child);
        child.setParent(this);
    }

    @Override
    public Container findChild(String name) {
        for (Container child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    @Override
    public List<Container> findChildren() {
        return children;
    }

    @Override
    public void removeChild(Container child) {
        children.remove(child);
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }

    @Override
    public Container map(HttpRequest request, boolean update) {
        return null;
    }


    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void start() {
        init();
        parseWebroot(HostConfig.WEB_ROOT);
        loader = new HostLoader();
        //子组件启动。
        children.forEach(c -> c.start());
        pipeline.start();
        loader.start();

        started  = true;
    }

    private void init() {
        pipeline = new SimplePipeline();
        pipeline.setBasic(new StandardHostValve(this));

        loader = new HostLoader();
    }

    /**
     * 解析webroot下的文件夹和war文件。
     *
     * @param webRoot
     */
    private void parseWebroot(String webRoot) {
        File file = new File(webRoot);
        if (file.isDirectory()) {
            for (File contextFile : file.listFiles()) {
                if (contextFile.isDirectory()) {
                    parseContextByDir(contextFile);
                } else if (contextFile.getName().endsWith(".war")) {
                    //TODO 解析War文件
                }
            }
        }
    }

    /**
     * 解析Context 以及映射url
     *
     * @param contextFile
     */
    private void parseContextByDir(File contextFile) {
        Context context = new StandardContext(this);
        ContextDirFile dirFile = new ContextDirFile(contextFile);
        context.setDirFile(dirFile);
        String uri = "/" + contextFile.getName();
        contextRegistry.put(uri, context);
        children.add(context);
    }

    @Override
    public void stop() {
        //子组件暂停
        children.forEach(c -> c.stop());

        pipeline.stop();

        loader.stop();

    }

    @Override
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public Pipeline getPipeline() {
        return pipeline;
    }

    @Override
    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
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
        this.loader = loader;
    }
}
