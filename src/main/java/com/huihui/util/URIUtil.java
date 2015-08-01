package com.huihui.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by hadoop on 2015/7/22 0022.
 */
public class URIUtil {
    public static URI getUri(String str){
        URI uri = null;
        try {
            uri = new URI(str);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }
}
