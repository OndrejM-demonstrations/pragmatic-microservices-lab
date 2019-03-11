package net.java.discovery;

public class ServiceAdvertisement {

    private String name;
    private String address;
    private Integer port;
    private String contextRoot;

    public String getName() {
        return name;
    }

    public ServiceAdvertisement withName(String name) {
        this.name = name;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public ServiceAdvertisement withPort(int port) {
        this.port = port;
        return this;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public ServiceAdvertisement withContextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ServiceAdvertisement withAddress(String address) {
        this.address = address;
        return this;
    }

}
