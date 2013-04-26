package tools;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * 开发调试使用的 Jetty Server
 * @author zhangdian
 */
public class JettyServer {

	public static void main(String[] args) throws Exception {
		Server server = buildNormalServer(8081, "/");
		server.start();
	}

	/**
	 * 创建用于正常运行调试的Jetty Server, 以src/main/webapp为Web应用目录.
	 */
	public static Server buildNormalServer(int port, String contextPath) {
		Server server = new Server(port);
		WebAppContext webContext = new org.mortbay.jetty.webapp.WebAppContext(
				"src/main/webapp", contextPath);
		// WebAppContext webContext = new
		// org.mortbay.jetty.webapp.WebAppContext("J:/JavaFile/web/target/sendCloud.war",
		// contextPath);
		webContext.setClassLoader(Thread.currentThread().getContextClassLoader());
		server.setHandler(webContext);
		server.setStopAtShutdown(true);
		return server;
	}
}
