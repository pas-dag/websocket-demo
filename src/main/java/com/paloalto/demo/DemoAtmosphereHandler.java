package com.paloalto.demo;

import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@AtmosphereHandlerService
public class DemoAtmosphereHandler implements AtmosphereHandler {
    private final static Logger log = LoggerFactory.getLogger(DemoAtmosphereHandler.class);

    public void onRequest(final AtmosphereResource atmosphereResource) throws IOException {
        // Register a listener/broadcaster for this users connection.
        BroadcasterFactory broadcasterFactory = atmosphereResource.getAtmosphereConfig().getBroadcasterFactory();
        final Broadcaster broadcaster = broadcasterFactory.get();

        atmosphereResource.suspend();

        broadcaster.addAtmosphereResource(atmosphereResource);

        log.info("begin {}", atmosphereResource.uuid());

        atmosphereResource.addEventListener(new AtmosphereResourceEventListenerAdapter() {
            @Override
            public void onDisconnect(AtmosphereResourceEvent event) {

                log.info("disconnect {} {}", atmosphereResource.uuid(), event);
                broadcaster.removeAtmosphereResource(atmosphereResource);
            }

            @Override
            public void onClose(AtmosphereResourceEvent event) {
                log.info("close {} {}", atmosphereResource.uuid(), event);
                broadcaster.removeAtmosphereResource(atmosphereResource);
            }

            @Override
            public void onHeartbeat(AtmosphereResourceEvent event) {
                log.info("heartbeat {} {}", atmosphereResource.uuid(), event);
            }

            @Override
            public void onBroadcast(AtmosphereResourceEvent event) {
                log.info("broadcast {} {}", atmosphereResource.uuid(), event);
            }

            @Override
            public void onSuspend(AtmosphereResourceEvent event) {
                log.info("suspend {} {}", atmosphereResource.uuid(), event);
            }

            @Override
            public void onResume(AtmosphereResourceEvent event) {
                log.info("resume {} {}", atmosphereResource.uuid(), event);
            }

            @Override
            public void onThrowable(AtmosphereResourceEvent event) {
                log.info("throwable {} {}", atmosphereResource.uuid(), event);
            }

            @Override
            public void onPreSuspend(AtmosphereResourceEvent event) {
                log.info("presuspend {} {}", atmosphereResource.uuid(), event);
            }
        });

        atmosphereResource.setBroadcaster(broadcaster);



    }

    public void onStateChange(AtmosphereResourceEvent event) throws IOException {
        AtmosphereResource resource = event.getResource();

        log.info("onStateChange {}", event);

        resource.write(event.getMessage().toString());

    }

    public void destroy() {

    }
}
