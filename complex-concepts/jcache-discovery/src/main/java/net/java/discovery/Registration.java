package net.java.discovery;

import java.io.Serializable;

public class Registration implements Serializable {
    private String id;
    private String name;
    private String address;
    private int port;
    private String contextRoot;

    public String getId() {
        return id;
    }

    public Registration id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Registration name(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Registration address(String address) {
        this.address = address;
        return this;
    }

    public int getPort() {
        return port;
    }

    public Registration port(int port) {
        this.port = port;
        return this;
    }

    public String getContextRoot() {
        return contextRoot;
    }

    public Registration contextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
        return this;
    }
    
    
}
