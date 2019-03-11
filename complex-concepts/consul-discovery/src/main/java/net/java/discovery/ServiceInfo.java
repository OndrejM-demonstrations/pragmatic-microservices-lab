package net.java.discovery;

import org.eclipse.microprofile.config.Config;

public class ServiceInfo {

    public static String getServiceAddress(Config config) {
        String serviceAddress = config.getValue("payara.instance.http.address", String.class);
        if (serviceAddress == null) {
            serviceAddress = config.getValue("http.address", String.class);
        }
        if (serviceAddress == null || serviceAddress.equals("0.0.0.0")) {
            serviceAddress = "localhost";
        }
        return serviceAddress;
    }

    public static Integer getServicePort(Config config) {
        Integer servicePort = config.getValue("payara.instance.http.port", Integer.class);
        if (servicePort == null) {
            servicePort = config.getValue("http.port", Integer.class);
        }
        if (servicePort == null) {
            servicePort = 80;
        }
        return servicePort;
    }

}
