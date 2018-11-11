package io.zino.servicedoni;

import io.zino.servicedoni.serviceregistery.ServiceRegisteryEngine;
import io.zino.servicedoni.servlet.RegistrationServlet;
import io.zino.servicedoni.servlet.ServiceRequestServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Servicedoni {
    private static final Logger logger = LoggerFactory.getLogger(Servicedoni.class);

    public static void main(String[] args) {

        Servicedoni servicedoni = new Servicedoni();
    }

    private Servicedoni() {
        ServiceRegisteryEngine.getInstance();

        try {
            this.runJetty();
        } catch (Exception e) {
            logger.error("Jetty cannot run correctly:", e);
        }
    }

    private void runJetty() throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.setConnectors(new Connector[]{connector});

        ServletHandler servletHandler = new ServletHandler();

        servletHandler.addServletWithMapping(RegistrationServlet.class, "/register");
        servletHandler.addServletWithMapping(ServiceRequestServlet.class, "/request");

        server.setHandler(servletHandler);
        server.start();
    }
}
