package com.huihui.simple;

import com.huihui.connector.RequestWrapper;
import com.huihui.connector.ResponseWrapper;
import com.huihui.core.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hadoop on 2015/7/25 0025.
 */
public class SimplePipeline implements Pipeline {
    private Valve  basic;
    private List<Valve> valves = new LinkedList<>();

    @Override
    public void invoke(RequestWrapper request, ResponseWrapper response) throws IOException, ServletException {
        for(Valve valve:valves){
            valve.invoke(request,response);
        }
        if(basic!=null)
            basic.invoke(request,response);
    }
    @Override
    public Valve getBasic() {
        return basic;

    }
    @Override
    public void setBasic(Valve basic) {
        this.basic = basic;
    }

    @Override
    public void addValve(Valve valve) {

    }

    @Override
    public List<Valve> getValves() {
        return valves;
    }
    @Override
    public void removeValve(Valve valve) {
        valves.remove(valve);
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
