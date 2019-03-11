package net.java.discovery;

import com.orbitz.consul.Consul;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.model.health.ServiceHealth;
import java.util.*;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class ConsulDiscoveryConfigSource implements ConfigSource {

    private static final String CONFIG_DISCOVERY_PREFIX = "discovery.";
    private static final String CONFIG_DISCOVERY_SERVICE_PREFIX = CONFIG_DISCOVERY_PREFIX + "service.";

    @Override
    public Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    public String getValue(String propertyName) {
        if (propertyName.startsWith(CONFIG_DISCOVERY_SERVICE_PREFIX)) {
            Consul consul = ConsulClient.build();
            String[] serviceNameAndProp = propertyName.split("\\.");
            if (serviceNameAndProp.length == 4) {
                String serviceName = serviceNameAndProp[2];
                String serviceProp = serviceNameAndProp[3];
                if (serviceProp.equals("url")) {
                    // propertyName is "discovery.service.NAME.url"
                    // result is a url that doesn't end with / and optionally includes context root
                    List<ServiceHealth> services = consul.healthClient()
                            .getHealthyServiceInstances(serviceName).getResponse();
                    if (!services.isEmpty()) {
                        int randomIndex = new Random().nextInt(services.size());
                        Service selectedService = services.get(randomIndex).getService();
                        String contextRoot = selectedService.getMeta().getOrDefault(ConsulClient.KEY_CTX_ROOT, "");
                        if ( !contextRoot.isEmpty() && !contextRoot.startsWith("/")) {
                            contextRoot = "/" + contextRoot;
                        }
                        StringBuilder resultBuilder = new StringBuilder();
                        resultBuilder = resultBuilder.append("http://").append(selectedService.getAddress())
                                .append(":").append(selectedService.getPort())
                                .append(contextRoot);
                        return resultBuilder.toString();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return ConsulDiscoveryConfigSource.class.getSimpleName();
    }

}
