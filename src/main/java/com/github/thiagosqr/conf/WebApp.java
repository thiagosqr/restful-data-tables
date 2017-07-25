package com.github.thiagosqr.conf;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class WebApp extends ResourceConfig {

    public WebApp() {
        packages("com.github.thiagosqr");
    }

}