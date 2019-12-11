package com.paloalto.demo;

import io.dropwizard.Configuration;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;

import javax.servlet.ServletRegistration;

public class DemoApplication extends Application<Configuration> {
    public static void main(final String[] args) throws Exception {
        DemoApplication application = new DemoApplication();
        application.run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        AtmosphereServlet atmosphereServlet = new AtmosphereServlet();

        atmosphereServlet.framework()
                .addInitParameter(ApplicationConfig.ANNOTATION_PACKAGE, DemoAtmosphereHandler.class.getPackage().getName());
        atmosphereServlet.framework().addInitParameter(ApplicationConfig.WEBSOCKET_CONTENT_TYPE, "application/json");
        atmosphereServlet.framework().addInitParameter(ApplicationConfig.WEBSOCKET_SUPPORT, "true");
        atmosphereServlet.framework().addInitParameter(ApplicationConfig.HEARTBEAT_INTERVAL_IN_SECONDS, "30");
        atmosphereServlet.framework().addInitParameter(ApplicationConfig.CLIENT_HEARTBEAT_INTERVAL_IN_SECONDS, "30");

        atmosphereServlet.framework().addInitParameter(ApplicationConfig.BROADCASTER_SHARABLE_THREAD_POOLS, "false");

        ServletRegistration.Dynamic registration = environment.servlets().addServlet("atmosphere", atmosphereServlet);
        registration.addMapping("/websocket/*");
    }

}
