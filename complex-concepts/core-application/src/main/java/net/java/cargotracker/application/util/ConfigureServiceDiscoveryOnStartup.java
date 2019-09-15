package net.java.cargotracker.application.util;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import net.java.cargotracker.infrastructure.routing.GraphTraversalService;

@Singleton
@Startup
public class ConfigureServiceDiscoveryOnStartup {
    
    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void configure() {
        System.setProperty("discovery.service.pathfinder.url", "pathfinder.url");
    }
}
