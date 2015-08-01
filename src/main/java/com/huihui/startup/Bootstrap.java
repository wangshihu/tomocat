package com.huihui.startup;

import com.huihui.connector.Connector;
import com.huihui.core.Host;
import com.huihui.simple.StandardHost;

import java.io.IOException;

/**
 * Created by hadoop on 2015/7/22 0022.
 */
public class Bootstrap {
    public static void main(String[] args) throws IOException {
        Connector connector = new Connector();
        Host host = new StandardHost();
        connector.setContainer(host);
        try {
            connector.start();
            System.in.read();
            connector.stop();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
