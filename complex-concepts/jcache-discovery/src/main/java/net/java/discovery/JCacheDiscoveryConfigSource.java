package net.java.discovery;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.*;
import javax.cache.*;
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
public class JCacheDiscoveryConfigSource implements ConfigSource {

    private static final String CONFIG_DISCOVERY_SERVICE_PREFIX = "discovery.service.";

    @Override
    public Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    public String getValue(String propertyName) {
        JCacheClient cacheClient;
        String propertyMapping = System.getProperty(CONFIG_DISCOVERY_SERVICE_PREFIX + propertyName);
        if (propertyMapping != null) {
            String[] serviceNameAndProp = propertyMapping.split("\\.");
            if (serviceNameAndProp.length == 2) {
                String serviceName = serviceNameAndProp[0];
                String serviceProp = serviceNameAndProp[1];
                if (serviceProp.equals("url")) {
                    // result is a url that doesn't end with / and optionally includes context root
                    cacheClient = new JCacheClient();
                    Cache<String, Registration> registrationCache
                            = cacheClient.getRegistrationCache(serviceName);
                    Spliterator<Cache.Entry<String, Registration>> itRegistrations = registrationCache.spliterator();
                    List<Registration> registrations = StreamSupport.stream(itRegistrations, false)
                            .map(Cache.Entry::getValue)
                            .collect(Collectors.toList());
                    if (!registrations.isEmpty()) {
                        Registration serviceInfo = selectRandomRegistration(registrations);
                        String propertyValue = serviceInfo.getUri().toString();
                        Logger.getLogger(JCacheDiscoveryConfigSource.class.getName()).info("Setting property " + propertyName + " to value " + propertyValue);
                        return propertyValue;
                    }
                }
            }
        }

        return null;
    }

    private static Registration selectRandomRegistration(List<Registration> registrations) {
        return registrations.get(new Random().nextInt(registrations.size()));
    }

    @Override
    public String
            getName() {
        return JCacheDiscoveryConfigSource.class
                .getSimpleName();
    }

}
