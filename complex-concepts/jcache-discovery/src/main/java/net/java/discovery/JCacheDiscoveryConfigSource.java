package net.java.discovery;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.*;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class JCacheDiscoveryConfigSource implements ConfigSource {

    private static final String CONFIG_DISCOVERY_PREFIX = "discovery.";
    private static final String CONFIG_DISCOVERY_SERVICE_PREFIX = CONFIG_DISCOVERY_PREFIX + "service.";

    @Override
    public Map<String, String> getProperties() {
        return new HashMap<>();
    }

    @Override
    public String getValue(String propertyName) {
        JCacheClient cacheClient;
        if (propertyName.startsWith(CONFIG_DISCOVERY_SERVICE_PREFIX)) {
            String[] serviceNameAndProp = propertyName.split("\\.");
            if (serviceNameAndProp.length == 4) {
                String serviceName = serviceNameAndProp[2];
                String serviceProp = serviceNameAndProp[3];
                if (serviceProp.equals("url")) {
                    // propertyName is "discovery.service.NAME.url"
                    // result is a url that doesn't end with / and optionally includes context root
                    cacheClient = new JCacheClient();
                    Cache<String, Registration> registrationCache
                            = cacheClient.getRegistrationCache(serviceName);
                    Iterator<Cache.Entry<String, Registration>> itRegistrations = registrationCache.iterator();
                    if (itRegistrations.hasNext()) {
                        Registration serviceInfo = itRegistrations.next().getValue();
                        String contextRoot = serviceInfo.getContextRoot();
                        if (!contextRoot.isEmpty() && !contextRoot.startsWith("/")) {
                            contextRoot = "/" + contextRoot;
                        }
                        StringBuilder resultBuilder = new StringBuilder();
                        resultBuilder = resultBuilder.append("http://").append(serviceInfo.getAddress())
                                .append(":").append(serviceInfo.getPort())
                                .append(contextRoot);
                        return resultBuilder.toString();
                    }
                }
            }
        }

        return null;
    }

    @Override
    public String
            getName() {
        return JCacheDiscoveryConfigSource.class
                .getSimpleName();
    }

}
