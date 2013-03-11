package com.github.raonifn.casperjs.junit;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CasperExecutor {

	private String casperPath = "casperjs";

	private List<String> env = new ArrayList<String>();

	private List<OutputStream> outs = new ArrayList<OutputStream>();

	private List<OutputStream> errs = new ArrayList<OutputStream>();

	public CasperExecutor(String casperPath) {
		this.casperPath = casperPath;
	}

	public CasperExecutor() {
	}

	public void addEnv(String name, String value) {
		this.env.add(name + "=" + value);
	}

	public int executeCasper(String pathToFile, String... arguments) {
		Runtime runtime = Runtime.getRuntime();
		File dir = new File(pathToFile).getParentFile();

		String phantomjs = System.getProperty("phantomjs.executable");
		if (phantomjs != null) {
			addEnv("PHANTOMJS_EXECUTABLE", phantomjs);
		}

		try {
			Process exec = runtime.exec(mountCommandLine(pathToFile, arguments), mountEnv(), dir);

			if (!outs.isEmpty()) {
				Pipe pipe = new Pipe(exec.getInputStream(), outs);
				pipe.start();
			}
			if (!errs.isEmpty()) {
				Pipe pipe = new Pipe(exec.getErrorStream(), errs);
				pipe.start();
			}

			int ret = exec.waitFor();

			return ret;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private String[] mountCommandLine(String pathToFile, String[] arguments) {
		List<String> cmd = new ArrayList<String>(arguments.length + 2);
		cmd.add(casperPath);
		cmd.add(pathToFile);
		cmd.addAll(Arrays.asList(arguments));
		return cmd.toArray(new String[cmd.size()]);
	}

	private String[] mountEnv() {
		Map<String, String> sysEnv = System.getenv();
		List<String> ret = new ArrayList<String>(sysEnv.size() + env.size());
		ret.addAll(env);

		for (Entry<String, String> envEntry : sysEnv.entrySet()) {
			ret.add(envEntry.getKey() + "=" + envEntry.getValue());
		}
		String[] envArray = ret.toArray(new String[ret.size()]);
		return envArray;
	}

	public void pipeOut(OutputStream out) {
		this.outs.add(out);
	}

	public void pipeErr(OutputStream err) {
		this.errs.add(err);
	}

}
