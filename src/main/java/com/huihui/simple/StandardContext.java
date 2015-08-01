package com.huihui.simple;

import com.huihui.connector.RequestWrapper;
import com.huihui.connector.ResponseWrapper;
import com.huihui.core.*;
import com.huihui.core.io.ContextDirFile;
import com.huihui.core.loader.WebappLoader;
import com.huihui.exceptions.LifecycleException;
import com.huihui.util.LifeCycleSupport;
import com.huihui.valves.StandardContextValve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hadoop on 2015/7/25 0025.
 */
public class StandardContext implements Context {

    private Pipeline pipeline;
    private Container parent;
    private Loader loader;
    private String info;
    final Logger logger = LoggerFactory.getLogger(StandardContext.class);
    private String name;
    private Map<String, Wrapper> servletMapping = new ConcurrentHashMap<>();
    private List<Container> children = Collections.synchronizedList(new ArrayList());
    private boolean started = false;

    private final LifeCycleSupport lifeCycleSupport = new LifeCycleSupport(this);

    ContextDirFile dirFile;

    public StandardContext(Container parent) {
        this.parent = parent;
    }

    @Override
    public void invoke(RequestWrapper request, ResponseWrapper response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }

    @Override
    public void addServletMapping(String pattern, Wrapper wrapper) {
        addChild(wrapper);
        servletMapping.put(pattern.toLowerCase(), wrapper);
    }


    @Override
    public Wrapper findServletMapping(String pattern) {
        return servletMapping.get(pattern);
    }

    @Override
    public Map<String, Wrapper> findServletMappings() {
        return servletMapping;
    }

    public ContextDirFile getDirFile() {
        return dirFile;
    }

    public void setDirFile(ContextDirFile dirFile) {
        this.dirFile = dirFile;
    }

    @Override
    public ClassLoader getParentClassLoader() {
        if(parent!=null&&parent.getLoader()!=null)
            return parent.getLoader().getClassLoader();
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
        Container result = null;
        for (Container container : children) {
            String containerName = container.getName();
            if (containerName != null && containerName.equals(name)) {
                result = container;
                break;
            }
        }
        return result;
    }

    @Override
    public List<Container> findChildren() {
        return children;
    }


    @Override
    public Container map(RequestWrapper request, boolean update) {
        return null;
    }

    @Override
    public void removeChild(Container child) {
        children.remove(child);
    }

    //----------------------------------------------------------properties get and set method
    @Override
    public Pipeline getPipeline() {
        if (pipeline == null) {
            pipeline = new SimplePipeline();
            pipeline.setBasic(new StandardContextValve(this));
        }
        return pipeline;
    }

    @Override
    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
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

    @Override
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
    public void addLifecycleListener(LifecycleListener listener) {
        lifeCycleSupport.addLifecycleListener(listener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifeCycleSupport.removeLifecycleListener(listener);
    }

    @Override
    public synchronized void start() {
        if (started)
            throw new LifecycleException("context is started");
        lifeCycleSupport.fireLifecycleEvent(LifeStatus.BEFORE_START_EVENT, null);
        this.name = dirFile.getSource().getName();
        //子组件启动。
        getPipeline().start();
        this.loader =  new WebappLoader(this);
        loader.start();
        new WebXmlParser(this).parser();//解析Web-xml;
        children.forEach(c -> c.start());
        lifeCycleSupport.fireLifecycleEvent(LifeStatus.START_EVENT, null);
        started = true;
        lifeCycleSupport.fireLifecycleEvent(LifeStatus.AFTER_START_EVENT, null);
    }



    @Override
    public synchronized void stop() {
        if (!started)
            throw new LifecycleException("context is not started");
        lifeCycleSupport.fireLifecycleEvent(LifeStatus.BEFORE_STOP_EVENT, null);
        //子组件暂停
        children.forEach(c -> c.stop());
        if (pipeline != null) {
            pipeline.stop();
        }
        if (loader != null) {
            loader.stop();
        }
        lifeCycleSupport.fireLifecycleEvent(LifeStatus.STOP_EVENT, null);
        lifeCycleSupport.fireLifecycleEvent(LifeStatus.AFTER_STOP_EVENT, null);
    }
}
