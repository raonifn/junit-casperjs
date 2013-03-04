package com.github.raonifn.casperjs.junit;

import java.net.URL;

import com.googlecode.mycontainer.cpscanner.ScannerListener;

public abstract class JavaScriptScannerListener implements ScannerListener {

    public void resourceFound(URL base, URL resource) {
        if (isJS(resource)) {
            jsFound(base, resource);
        }
    }

    private boolean isJS(URL url) {
        return url.toString().endsWith(".test.js");
    }

    public abstract void jsFound(URL base, URL resource);
}
