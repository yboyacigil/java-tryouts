package tryouts.guice.rest;

import com.google.inject.servlet.GuiceFilter;
import org.apache.catalina.Context;
import org.apache.catalina.deploy.FilterDef;
import org.apache.catalina.deploy.FilterMap;
import org.apache.catalina.startup.Tomcat;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.File;

public class GreetingApplication {

    public static void main(String[] args) throws Exception {
        // startJettyServer();
        startTomcatServer();
    }

    private static void startJettyServer() throws Exception {
        Server server = new Server(8080);

        ServletContextHandler contextHandler = new ServletContextHandler(server, "/");
        contextHandler.addEventListener(new GreetingServiceContextListener());
        contextHandler.addFilter(GuiceFilter.class, "/*", null);
        contextHandler.addServlet(DefaultServlet.class, "/");

        server.start();
        server.join();
    }

    private static void startTomcatServer() throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        // ??? not working
        Context context = tomcat.addContext("", "guice-rest");

        FilterDef guiceFilterDef = new FilterDef();
        guiceFilterDef.setFilterClass(GuiceFilter.class.getName());
        guiceFilterDef.setFilterName("guiceFilter");
        context.addFilterDef(guiceFilterDef);

        FilterMap guiceFilterMap = new FilterMap();
        guiceFilterMap.addURLPattern("/*");
        guiceFilterMap.setFilterName("guiceFilter");
        context.addFilterMap(guiceFilterMap);

        context.addApplicationListener(GreetingServiceContextListener.class.getName());

        tomcat.start();
        tomcat.getServer().await();
    }

}
