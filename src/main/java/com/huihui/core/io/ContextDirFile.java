package com.huihui.core.io;

import java.io.File;

/**
 * Created by hadoop on 2015/7/28 0028.
 */
public class ContextDirFile {
    File source;

    public ContextDirFile(File source) {
        this.source = source;
    }

    public File getSource() {
        return source;
    }

    public void setSource(File source) {
        this.source = source;
    }
}
