package com.github.raonifn.casperjs.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.raonifn.casperjs.junit.CasperEnvironment;
import com.github.raonifn.casperjs.junit.JUnitMethodCaller;


public class JUnitMethodCallerTest {

	public static class Tester {

		private boolean beforeCalled = false;
		private boolean afterCalled = false;
		private static boolean beforeClassCalled = false;
		private static boolean afterClassCalled = false;

		public static void reset() {
			beforeClassCalled = false;
			afterClassCalled = false;
		}

		@Before
		public void before() {
			this.beforeCalled = true;
		}

		@After
		public void after() {
			this.afterCalled = true;
		}

		@BeforeClass
		public static void beforeClass() {
			beforeClassCalled = true;
		}

		@AfterClass
		public static void afterClass() {
			afterClassCalled = true;
		}

		public boolean isBeforeCalled() {
			return beforeCalled;
		}

		public boolean isAfterCalled() {
			return afterCalled;
		}

		public static boolean isBeforeClassCalled() {
			return beforeClassCalled;
		}

		public static boolean isAfterClassCalled() {
			return afterClassCalled;
		}

		@CasperEnvironment
		public Map<String, String> getEnv() {
			Map<String, String> ret = new HashMap<String, String>();

			ret.put("TESTE", "ABC");

			return ret;
		}

	}

	private JUnitMethodCaller caller;

	@Before
	public void init() {
		Tester.reset();
		caller = new JUnitMethodCaller(Tester.class);
	}

	@Test
	public void testBefore() {
		caller.instanciate();
		Tester tester = (Tester) caller.getTarget();
		caller.before();
		assertTrue(tester.isBeforeCalled());
		assertFalse(tester.isAfterCalled());
		assertFalse(Tester.isBeforeClassCalled());
		assertFalse(Tester.isAfterClassCalled());
	}

	@Test
	public void testAfter() {
		caller.instanciate();
		Tester tester = (Tester) caller.getTarget();
		caller.after();
		assertFalse(tester.isBeforeCalled());
		assertTrue(tester.isAfterCalled());
		assertFalse(Tester.isBeforeClassCalled());
		assertFalse(Tester.isAfterClassCalled());
	}

	@Test
	public void testBeforeClass() {
		caller.beforeClass();
		assertTrue(Tester.isBeforeClassCalled());
		assertFalse(Tester.isAfterClassCalled());
	}

	@Test
	public void testAfterClass() {
		caller.afterClass();
		assertFalse(Tester.isBeforeClassCalled());
		assertTrue(Tester.isAfterClassCalled());
	}

	@Test
	public void testCasperEnvironment() {
		caller.instanciate();
		Map<String, String> env = caller.getCasperEnvironment();
		assertEquals("ABC", env.get("TESTE"));
	}

}
