package com.github.raonifn.casperjs.junit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class JUnitMethodCaller {

	private Class<?> clazz;

	private Object target;

	private Method before;

	private Method beforeClass;

	private Method after;

	private Method afterClass;

	private Method environment;

	public JUnitMethodCaller(Class<?> clazz) {
		this.clazz = clazz;

		for (Method method : clazz.getMethods()) {
			beforeMethod(method);
			afterMethod(method);
			beforeClassMethod(method);
			afterClassMethod(method);
			environmentMethod(method);
		}
	}

	private void beforeMethod(Method method) {
		if (before == null && method.isAnnotationPresent(Before.class)) {
			this.before = method;
		}
	}

	private void afterMethod(Method method) {
		if (after == null && method.isAnnotationPresent(After.class)) {
			this.after = method;
		}
	}

	private void beforeClassMethod(Method method) {
		if (beforeClass == null && method.isAnnotationPresent(BeforeClass.class)) {
			this.beforeClass = method;
		}
	}

	private void afterClassMethod(Method method) {
		if (afterClass == null && method.isAnnotationPresent(AfterClass.class)) {
			this.afterClass = method;
		}
	}

	private void environmentMethod(Method method) {
		if (environment != null)
			return;
		if (!method.isAnnotationPresent(CasperEnvironment.class)) {
			return;
		}
		if (!method.getReturnType().equals(Map.class)) {
			throw new RuntimeException("CasperEnvironment must return java.util.Map");
		}

		this.environment = method;
	}

	public void instanciate() {
		try {
			target = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public Object getTarget() {
		return target;
	}

	public void before() {
		if (before != null) {
			invoke(target, before);
		}
	}

	public void beforeClass() {
		if (beforeClass != null) {
			invoke(null, beforeClass);
		}
	}

	public void after() {
		if (after != null) {
			invoke(target, after);
		}
	}

	public void afterClass() {
		if (afterClass != null) {
			invoke(null, afterClass);
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getCasperEnvironment() {
		if (environment != null) {
			return (Map<String, String>) invoke(target, environment);
		}
		return Collections.<String, String> emptyMap();
	}

	private Object invoke(Object target, Method method) {
		try {
			return method.invoke(target, new Object[0]);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

}
