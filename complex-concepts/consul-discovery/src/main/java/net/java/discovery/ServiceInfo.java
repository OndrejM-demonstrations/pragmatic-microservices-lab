package net.java.discovery;

import org.eclipse.microprofile.config.Config;

public class ServiceInfo {

    public static String getServiceAddress(Config config, String defaultValue) {
        String serviceAddress = config.getOptionalValue("http.address", String.class)
                .orElseGet(() -> {
                    if (defaultValue != null) {
                        return defaultValue;
                    } else {
                        return config.getOptionalValue("payara.instance.http.address", String.class)
                                .orElse("localhost");
                    }
                });
        if (serviceAddress.equals("0.0.0.0")) {
            serviceAddress = "localhost";
        }
        return serviceAddress;
    }

    public static Integer getServicePort(Config config, Integer defaultValue) {
        Integer servicePort = config.getOptionalValue("http.port", Integer.class)
                .orElseGet(() -> {
                    if (defaultValue != null) {
                        return defaultValue;
                    } else {
                        return config.getOptionalValue("payara.instance.http.port", Integer.class)
                                .orElse(80);
                    }
                });
        return servicePort;
    }
}
