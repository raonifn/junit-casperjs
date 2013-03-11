package com.github.raonifn.casperjs.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CasperExecutorTest {

	@Test
	public void testCasperjsLocation() {
		URL resource = Thread.currentThread().getContextClassLoader().getResource("dummy.test.js");
		String file = resource.getPath();

		CasperExecutor executor = createExecutor();
		assertEquals(0, executor.executeCasper(file));

		System.setProperty("casperjs.executable", "./foo.bar");
		executor = createExecutor();
		try {
			executor.executeCasper(file);
			fail("Must throw exception");
		} catch (RuntimeException ex) {
			assertTrue(ex.getMessage().contains("./foo.bar"));
		}

	}

	@After
	public void after() {
		System.clearProperty("casperjs.executable");
	}

	private CasperExecutor createExecutor() {
		CasperExecutor executor = new CasperExecutor();
		executor.pipeOut(System.out);
		executor.pipeErr(System.err);
		return executor;
	}
}
