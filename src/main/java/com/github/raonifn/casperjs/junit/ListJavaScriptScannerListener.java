package com.github.raonifn.casperjs.junit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListJavaScriptScannerListener extends JavaScriptScannerListener {

    private List<URL> javaScripts = new ArrayList<URL>();

    @Override
    public void jsFound(URL base, URL resource) {
        javaScripts.add(resource);
    }

    public List<URL> getJavaScripts() {
        return javaScripts;
    }

}
