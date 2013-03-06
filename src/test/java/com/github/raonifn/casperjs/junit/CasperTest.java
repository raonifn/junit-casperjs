package com.github.raonifn.casperjs.junit;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.googlecode.mycontainer.kernel.ShutdownCommand;
import com.googlecode.mycontainer.kernel.boot.ContainerBuilder;
import com.googlecode.mycontainer.web.ContextWebServer;
import com.googlecode.mycontainer.web.ServletDesc;
import com.googlecode.mycontainer.web.jetty.JettyServerDeployer;

@RunWith(CasperRunner.class)
public class CasperTest {
	private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(CasperTest.class);

	private ContainerBuilder builder;

	@Before
	public void before() throws Exception {
		builder = new ContainerBuilder();
		builder.deployVMShutdownHook();

		JettyServerDeployer webDeployer = builder.createDeployer(JettyServerDeployer.class);
		webDeployer.setName("WebServer");
		webDeployer.bindPort(9898);

		ContextWebServer webContext = webDeployer.createContextWebServer();
		webContext.setContext("/casper");
		webContext.getServlets().add(new ServletDesc(new HttpServlet() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				resp.setContentType("text/html");
				PrintWriter out = resp.getWriter();
				out.println("<html><body><h1>Hello World</h1></body></html>");
			}

		}, "/*"));

		webDeployer.deploy();
	}

	@CasperEnvironment
	public Map<String, String> env() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("START_URL", "http://localhost:9898/casper");
		return map;

	}

	@After
	public void after() {
		try {
			ShutdownCommand shutdown = new ShutdownCommand();
			shutdown.setContext(builder.getContext());
			shutdown.shutdown();
		} catch (Exception e) {
			LOG.error("Error shutdown", e);
		}
	}
}
