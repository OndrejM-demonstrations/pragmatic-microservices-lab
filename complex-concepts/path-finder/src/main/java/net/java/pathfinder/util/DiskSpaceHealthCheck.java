package net.java.pathfinder.util;

import java.io.File;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Health
@ApplicationScoped
public class DiskSpaceHealthCheck implements HealthCheck {

    @Inject
    @ConfigProperty(name = "DiskSpaceHealthCheck.requiredFreeSpaceMB", defaultValue = "100")
    int requiredFreeSpaceMB;

    @Override
    public HealthCheckResponse call() {
        long freeSpace = new File(".").getFreeSpace();
        boolean isUp = (freeSpace >= requiredFreeSpaceMB * 1024 * 1024);
        return HealthCheckResponse.named("diskSpace")
                .withData("freeSpace", freeSpace)
                .withData("freeSpaceMB", freeSpace / 1024 / 1024)
                .withData("requiredFreeSpaceMB", requiredFreeSpaceMB)
                .state(isUp)
                .build();
    }
}
