package com.huihui.core;

import com.huihui.core.context.Context;

import java.net.URLClassLoader;

/**
 * Created by hadoop on 2015/7/28 0028.
 */
public interface Host extends Container {
    Context getContext(String uri);

    URLClassLoader getRootClassLoader();

}
