package com.paloalto.demo;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import io.dropwizard.Configuration;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRegistration;

public class DemoApplication extends Application<Configuration> {
    static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

    public static void main(final String[] args) throws Exception {
        DemoApplication application = new DemoApplication();
        resetLogConfig();
        application.run(args);
        resetLogConfig();
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

    static void resetLogConfig() {
        try {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            context.reset();
            ContextInitializer initializer = new ContextInitializer(context);
            initializer.autoConfig();
        } catch (JoranException e) {
            log.error("Oh no", e);
        }
    }
}
