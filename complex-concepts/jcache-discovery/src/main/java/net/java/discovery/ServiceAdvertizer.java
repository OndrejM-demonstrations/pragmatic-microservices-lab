package net.java.discovery;

import java.util.concurrent.*;
import javax.cache.*;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

public class ServiceAdvertizer {

    SelfHealthService serviceHealth = new SelfHealthService();

    public void advertize(ServiceAdvertisement advertisement) {
        advertize(advertisement, Executors.newSingleThreadScheduledExecutor());
    }

    public void advertize(ServiceAdvertisement advertisement, ScheduledExecutorService scheduler) {
        Config config = ConfigProvider.getConfig();
        String serviceAddress = getAddressFromAdvert(advertisement, config);
        Integer servicePort = getPortFromAdvert(advertisement, config);

        Registration service = new Registration()
                .id(idFromAdvert(advertisement))
                .name(advertisement.getName())
                .address(serviceAddress)
                .port(servicePort)
                .contextRoot(advertisement.getContextRoot());
        register(service);
        // keep refreshing the status within the TTL
        scheduler.scheduleAtFixedRate(() -> refreshAdvertisement(service), 2, 2, TimeUnit.SECONDS);
    }

    public void refreshAdvertisement(Registration service) {
        register(service);
    }

    private void register(Registration service) {
        JCacheClient cacheClient = new JCacheClient();
        Cache<String, Registration> registrationCache = cacheClient.getRegistrationCache(service.getName());
        registrationCache.put(service.getId(), service);
    }

    public void stopAdvertizing(ServiceAdvertisement advertisement) {
        JCacheClient cacheClient = new JCacheClient();
        Cache<String, Registration> registrationCache
                = cacheClient.getRegistrationCache(advertisement.getName());
        String serviceId = idFromAdvert(advertisement);
        registrationCache.remove(serviceId);
    }

    private String idFromAdvert(ServiceAdvertisement advertisement) {
        Config config = ConfigProvider.getConfig();
        String serviceAddress = getAddressFromAdvert(advertisement, config);
        Integer servicePort = getPortFromAdvert(advertisement, config);
        return advertisement.getName() + ":" + serviceAddress + ":" + servicePort;
    }

    private static Integer getPortFromAdvert(ServiceAdvertisement advertisment, Config config) {
        Integer servicePort = advertisment.getPort();
        if (servicePort == null) {
            servicePort = ServiceInfo.getServicePort(config);
        }
        return servicePort;
    }

    private static String getAddressFromAdvert(ServiceAdvertisement advertisment, Config config) {
        String serviceAddress = advertisment.getAddress();
        if (serviceAddress == null) {
            serviceAddress = ServiceInfo.getServiceAddress(config);
        }
        return serviceAddress;
    }

}
