package net.java.discovery;

import com.orbitz.consul.Consul;
import org.eclipse.microprofile.config.ConfigProvider;

public final class ConsulClient {

    static final String CONFIG_CONSUL_URL = "consul.url";
    static final String KEY_CTX_ROOT = "contextRoot";

    // only static methods here
    private ConsulClient() {
    }
    
    public static Consul build() {
        Consul.Builder consulBuilder = Consul.builder();
        ConfigProvider.getConfig()
                .getOptionalValue(CONFIG_CONSUL_URL, String.class)
                .ifPresent(url -> {
                    consulBuilder.withUrl(url);
                });
        return consulBuilder.build();
    }
}
