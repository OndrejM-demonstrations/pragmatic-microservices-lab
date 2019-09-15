package net.java.pathfinder.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

/**
 *
 * @author Ondrej Mihalyi
 */
@Health
@ApplicationScoped
public class ServiceActiveHealthCheck implements HealthCheck {
    
    @Inject
    @ConfigProperty(name = "ServiceActiveHealthCheck.timeToBecomeInactiveInSeconds", defaultValue = "10")
    int limitForInactivityInSeconds;
    
    @Inject
    LastResponseFilter lastResponseFilter;

    @Override
    public HealthCheckResponse call() {
        long inactiveForMillis = System.currentTimeMillis() - lastResponseFilter.getLastResponseCompletedTimeMillis(); 
        boolean isUp = (inactiveForMillis <= limitForInactivityInSeconds * 1000);
        return HealthCheckResponse.named("inactivity")
                .withData("last-response-timestamp", lastResponseFilter.getLastResponseCompletedTimeMillis())
                .withData("last-active-before-seconds", inactiveForMillis / 1000)
                .state(isUp)
                .build();
    }
}
