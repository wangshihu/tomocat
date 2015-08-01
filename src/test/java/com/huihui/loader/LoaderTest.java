package com.huihui.loader;

import com.huihui.simple.HostLoader;
import org.junit.Test;

/**
 * Created by hadoop on 2015/7/31 0031.
 */
public class LoaderTest {
    @Test
    public void testHostLoader() throws ClassNotFoundException {
        HostLoader loader = new HostLoader();
        loader.getClassLoader().loadClass("javax.servlet.Servlet");
    }
}
