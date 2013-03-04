package com.github.raonifn.casperjs.junit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

import com.googlecode.mycontainer.cpscanner.ClasspathScanner;

public class CasperRunner extends ParentRunner<CasperJSTestCase> {

	private final Class<?> clazz;

	private JUnitMethodCaller jUnitMethodCaller;

	public CasperRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
		this.clazz = clazz;

		this.jUnitMethodCaller = new JUnitMethodCaller(clazz);
	}

	@Override
	protected List<CasperJSTestCase> getChildren() {
		List<URL> urls = findJS(clazz);
		List<CasperJSTestCase> ret = new ArrayList<CasperJSTestCase>(urls.size());

		for (URL url : urls) {
			CasperJSTestCase testCase = new CasperJSTestCase();
			testCase.setUrl(clazz, url);
			ret.add(testCase);
		}

		return ret;
	}

	@Override
	protected Description describeChild(CasperJSTestCase child) {
		return child.getDescription();
	}

	@Override
	protected void runChild(CasperJSTestCase child, RunNotifier notifier) {
		jUnitMethodCaller.instanciate();
		child.run(notifier, jUnitMethodCaller);
	}

	private List<URL> findJS(Class<?> clazz) {
		ClasspathScanner classpathScanner = new ClasspathScanner();
		ListJavaScriptScannerListener listScannerListener = new ListJavaScriptScannerListener();
		classpathScanner.addListener(listScannerListener);

		classpathScanner.scan(clazz);

		List<URL> urls = listScannerListener.getJavaScripts();
		return urls;
	}

}
