package net.java.pathfinder.health;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.health.*;

@Health
@ApplicationScoped
public class ServiceDisabledHealthCheck implements HealthCheck {
    
    private boolean enabled = true;
    
    public void disable() {
        enabled = false;
    }
    
    @Override
    public HealthCheckResponse call() {
        return HealthCheckResponse.named("disabled")
                .state(enabled)
                .build();
    }
}
