package com.huihui.connector;

import com.huihui.core.*;
import com.huihui.exceptions.LifecycleException;
import com.huihui.net.DefaultServerSocketFactory;
import com.huihui.net.ServerSocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by hadoop on 2015/7/22 0022.
 */
public class Connector implements Runnable, Lifecycle {

    private Container container;
    private ServerSocketFactory factory;
    private String scheme = "http";


    private int port = 8080;
    private String host = "127.0.0.1";

    private Queue<HttpProcessor> processorPool = new LinkedList<>();

    private int minProcessors = 5;
    private int maxProcessors = 10;
    private int curProcessors;


    private Thread thread;

    private boolean started = false;

    public Connector() {
    }

    public Connector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            ServerSocket server = getFactory().createSocket(port, 1, InetAddress.getByName(host));
            while (started) {
                Socket socket = server.accept();
                HttpProcessor processor = getPorcessor();
                processor.assign(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void start() {
        initialize();
        thread = new Thread(this, "Connector tread");
        started=true;
        thread.start();
        if (container != null) {
            container.start();
        }
    }

    @Override
    public void stop() {
        if (container != null) {
            container.stop();
        }
        started=false;

    }

    // ------------------------------------------------------------- Properties


    /**
     * Create (or allocate) and return a Request object suitable for
     * specifying the contents of a Request to the responsible Container.
     */
    public HttpRequest createRequest() {
        HttpRequest request = new HttpRequest(this);
        return request;
    }


    /**
     * Create (or allocate) and return a Response object suitable for
     * receiving the contents of a Response from the responsible Container.
     */
    public HttpResponse createResponse() {
        HttpResponse response = new HttpResponse(this);
        return response;
    }

    /**
     * Invoke a pre-startup initialization. This is used to allow connectors
     * to bind to restricted ports under Unix operating environments.
     *
     * @throws LifecycleException If this server was already initialized.
     */
    public void initialize() {
        for (; curProcessors < minProcessors; ) {
            createProcessor(true);
        }
    }

    public HttpProcessor getPorcessor() {
        HttpProcessor processor = processorPool.poll();
        if (processor == null && curProcessors < maxProcessors) {
            processor = createProcessor(false);
        }
        return processor;
    }

    public HttpProcessor createProcessor(boolean add) {
        HttpProcessor processor = new HttpProcessor(this, ++curProcessors);
        if (add) {
            processorPool.add(processor);
        }
        processor.start();
        return processor;
    }

    public void recycle(HttpProcessor processor) {
        processorPool.add(processor);
    }


    public ServerSocketFactory getFactory() {
        if (this.factory == null)
            this.factory = new DefaultServerSocketFactory();
        return factory;
    }


    public int getMinProcessors() {
        return minProcessors;
    }

    public void setMinProcessors(int minProcessors) {
        this.minProcessors = minProcessors;
    }

    public int getMaxProcessors() {
        return maxProcessors;
    }

    public void setMaxProcessors(int maxProcessors) {
        this.maxProcessors = maxProcessors;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getCurProcessors() {
        return curProcessors;
    }

    public void setCurProcessors(int curProcessors) {
        this.curProcessors = curProcessors;
    }
    //    /**
//     * Return descriptive information about this Connector implementation.
//     */
//    public String getInfo();
//
//
//    /**
//     * Return the port number to which a request should be redirected if
//     * it comes in on a non-SSL port and is subject to a security constraint
//     * with a transport guarantee that requires SSL.
//     */
//    public int getRedirectPort();
//
//
//    /**
//     * Set the redirect port number.
//     *
//     * @param redirectPort The redirect port number (non-SSL to SSL)
//     */
//    public void setRedirectPort(int redirectPort);


    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }


//    /**
//     * Return the secure connection flag that will be assigned to requests
//     * received through this connector.  Default value is "false".
//     */
//    public boolean getSecure();
//
//
//    /**
//     * Set the secure connection flag that will be assigned to requests
//     * received through this connector.
//     *
//     * @param secure The new secure connection flag
//     */
//    public void setSecure(boolean secure);


//    /**
//     * Return the <code>Service</code> with which we are associated (if any).
//     */
//    public Service getService();
//
//
//    /**
//     * Set the <code>Service</code> with which we are associated (if any).
//     *
//     * @param service The service that owns this Engine
//     */
//    public void setService(Service service);


    // --------------------------------------------------------- Public Methods


    //    /**
//     * Return the "enable DNS lookups" flag.
//     */
//    public boolean getEnableLookups();
//
//
//    /**
//     * Set the "enable DNS lookups" flag.
//     *
//     * @param enableLookups The new "enable DNS lookups" flag value
//     */
//    public void setEnableLookups(boolean enableLookups);

}
