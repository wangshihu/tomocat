package com.huihui.connector;


import javax.servlet.ServletException;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hadoop on 2015/7/22 0022.
 */
public class HttpProcessor implements Runnable {
    private final Connector connector;
    private final int id;
    private final RequestWrapper requestWrapper;
    private final ResponseWrapper responseWrapper;
    private final String threadName;
    private boolean stoped = false;
    private Socket socket;

    private boolean available = true;
    ReentrantLock lock = new ReentrantLock();

    public HttpProcessor(Connector connector, int id) {
        this.connector = connector;
        this.id = id;
        this.requestWrapper = connector.createRequest();
        this.responseWrapper = connector.createResponse();
        this.requestWrapper.setResponseWrapper(responseWrapper);
        this.responseWrapper.setRequestWrapper(requestWrapper);
        this.threadName =
                "HttpProcessor[" + connector.getPort() + "][" + id + "]";
    }

    public void process(Socket socket) throws IOException {
        requestWrapper.refresh(socket);
        responseWrapper.refresh(socket);
        try {
            connector.getContainer().invoke(requestWrapper, responseWrapper);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        socket.close();
    }

    @Override
    public void run() {
        while (!stoped) {
            Socket socket = await();
            try {
                process(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.connector.recycle(this);
            // Tell threadStop() we have shut ourselves down successfully
        }
    }

    public synchronized void assign(Socket socket) {
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.socket = socket;
        available = false;
        notifyAll();
    }

    public synchronized Socket await() {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Socket socket = this.socket;
        available = true;
        notifyAll();
        return socket;
    }

    public void start() {
        Thread thread = new Thread(this,threadName);
        thread.start();
    }
}
