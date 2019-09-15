package net.java.discovery;

import com.orbitz.consul.Consul;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.model.health.ServiceHealth;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;
import org.eclipse.microprofile.config.spi.ConfigSource;

/**
  A service property in the form service-name.property must be mapped to a system property 
  with the name discovery.service.target-mp-config-property. For example, to map URL 
  of the pathfinder service into MP config property named pathfinderurl, specify 
  the following system property: discovery.service.pathfinderurl=pathfinder.url. 
  The system property needs to be defined before the MP config property pathfinderurl 
  is retrieved by the application and then the config source provides the pathfinder service URL. 
  Otherwise it doesn't provide any value for the MP Config property.
*/
public class ConsulDiscoveryConfigSource implements ConfigSource {

    private static final String CONFIG_DISCOVERY_SERVICE_PREFIX = "discovery.service.";

    @Override
    public Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    public String getValue(String propertyName) {
        String propertyMapping = System.getProperty(CONFIG_DISCOVERY_SERVICE_PREFIX + propertyName);
        if (propertyMapping != null) {
            Consul consul = ConsulClient.build();
            String[] serviceNameAndProp = propertyMapping.split("\\.");
            if (serviceNameAndProp.length == 2) {
                String serviceName = serviceNameAndProp[0];
                String serviceProp = serviceNameAndProp[1];
                if (serviceProp.equals("url")) {
                    // result is a url that doesn't end with / and optionally includes context root
                    List<ServiceHealth> services = consul.healthClient()
                            .getHealthyServiceInstances(serviceName).getResponse();
                    if (!services.isEmpty()) {
                        Service selectedService = selectRandomService(services).getService();
                        String contextRoot = selectedService.getMeta().getOrDefault(ConsulClient.KEY_CTX_ROOT, "");
                        String propertyValue = composeUri(selectedService.getAddress(), selectedService.getPort(), contextRoot)
                                .toString();
                        Logger.getLogger(this.getClass().getName()).info("Setting property " + propertyName + " to value " + propertyValue);
                        return propertyValue;
                    }
                }
            }
        }
        return null;
    }

    private static ServiceHealth selectRandomService(List<ServiceHealth> services) {
        return services.get(new Random().nextInt(services.size()));
    }
    
    private static URI composeUri(String address, int port, String contextRoot) {
        try {
            return new URI("http", null, address, port, contextRoot, null, null);
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

}
