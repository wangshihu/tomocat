package com.huihui.valves;

import com.huihui.connector.RequestWrapper;
import com.huihui.connector.ResponseWrapper;
import com.huihui.core.*;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by hadoop on 2015/7/25 0025.
 */
public class StandardContextValve implements Valve,Contained {
    private Container container;
    private String info;

    public StandardContextValve(Container container) {
        this.container = container;
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
    public String getInfo() {
        return info;
    }

    @Override
    public void invoke(RequestWrapper requestWrapper, ResponseWrapper responseWrapper) throws IOException, ServletException {

        String pattern = requestWrapper.getRequest().getRequestURI();
        Wrapper wrapper = ((Context)container).findServletMapping(pattern.toLowerCase());
        wrapper.invoke(requestWrapper,responseWrapper);
    }
}
