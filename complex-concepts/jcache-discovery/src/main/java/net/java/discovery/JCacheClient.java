package net.java.discovery;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.cache.*;
import javax.cache.configuration.Configuration;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.Duration;
import javax.cache.expiry.ModifiedExpiryPolicy;
import javax.naming.*;

public final class JCacheClient {

    CacheManager cacheManager;

    public JCacheClient() {
        try {
            Context ctx = new InitialContext();
            this.cacheManager = (CacheManager) ctx.lookup("payara/CacheManager");
        } catch (NamingException ex) {
            Logger.getLogger(JCacheClient.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    private <K, V> Cache<K, V> getOrCreateCache(String name, Configuration<K, V> config) {
        Cache<K, V> cache = cacheManager.getCache(name);
        if (cache == null) {
            try {
                cache = cacheManager.createCache(name, config);
            } catch (CacheException e) {
                cache = cacheManager.getCache(name);
            }
        }
        return cache;
    }

    public Cache<String, Registration> getRegistrationCache(String serviceName) {
        MutableConfiguration<String, Registration> cacheConfig = new MutableConfiguration<>();
        cacheConfig.setExpiryPolicyFactory(ModifiedExpiryPolicy.factoryOf(
                new Duration(TimeUnit.SECONDS, 3)));
        return getOrCreateCache("service:" + serviceName, cacheConfig);
    }

}
