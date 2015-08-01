package com.huihui.valves;

import com.huihui.connector.RequestWrapper;
import com.huihui.connector.ResponseWrapper;
import com.huihui.core.*;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by hadoop on 2015/7/25 0025.
 */
public class StandardHostValve implements Valve,Contained {
    private Container container;
    private String info;

    public StandardHostValve(Container container) {
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
        Context context = requestWrapper.getContext();
        if(context!=null)
            context.invoke(requestWrapper,responseWrapper);
    }
}