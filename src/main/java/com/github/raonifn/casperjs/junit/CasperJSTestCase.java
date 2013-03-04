package com.github.raonifn.casperjs.junit;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class CasperJSTestCase {

	private URL jsUrl;

	private Description description;

	private String path;

	public CasperJSTestCase() {
	}

	public void setUrl(Class<?> clazz, URL jsUrl) {
		this.jsUrl = jsUrl;
		init(clazz);
	}

	private void init(Class<?> clazz) {
		path = jsUrl.getPath();
		description = Description.createTestDescription(clazz, path);

	}

	public Description getDescription() {
		return description;
	}

	public void run(RunNotifier notifier, JUnitMethodCaller jUnitMethodCaller) {
		notifier.fireTestStarted(description);
		try {
			jUnitMethodCaller.before();
			try {
				CasperExecutor executor = new CasperExecutor();

				for (Map.Entry<String, String> mapEnv : jUnitMethodCaller.getCasperEnvironment().entrySet()) {
					executor.addEnv(mapEnv.getKey(), mapEnv.getValue());
				}

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				executor.pipeOut(out);
				executor.pipeOut(System.out);
				int result = executor.executeCasper(path);
				if (result == 0) {
					notifier.fireTestFinished(description);
					return;
				}

				String msg = readMessage(out);
				AssumptionViolatedException ex = new AssumptionViolatedException(msg);
				notifier.fireTestFailure(new Failure(description, ex));

			} catch (Exception ex) {
				notifier.fireTestFailure(new Failure(description, ex));
			} finally {
				jUnitMethodCaller.after();
			}

		} finally {
			notifier.fireTestFinished(description);
		}
	}

	private String readMessage(ByteArrayOutputStream out) throws IOException {
		byte[] array = out.toByteArray();
		ByteArrayInputStream bais = new ByteArrayInputStream(array);
		InputStreamReader reader = new InputStreamReader(bais);
		BufferedReader bufferedReader = new BufferedReader(reader);
		StringBuilder msgBuffer = new StringBuilder();
		String line;
		do {
			line = bufferedReader.readLine();
			if (msgBuffer.length() > 0) {
				msgBuffer.append('\n');
			}
			if (line != null) {
				msgBuffer.append(line);
			}
		} while (line != null);

		String msg = msgBuffer.toString();
		return msg;
	}
}
