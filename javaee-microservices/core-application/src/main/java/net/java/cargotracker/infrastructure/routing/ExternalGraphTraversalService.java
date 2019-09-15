package net.java.cargotracker.infrastructure.routing;

import java.util.*;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

@Stateless
public class ExternalGraphTraversalService {

    @Resource(name = "graphTraversalUrl")
    private String graphTraversalUrl;

    private final Client jaxrsClient = ClientBuilder.newClient();
    private WebTarget graphTraversalResource;

    @PostConstruct
    public void init() {
        graphTraversalResource = jaxrsClient.target(graphTraversalUrl);
    }

    public List<TransitPath> findShortestPath(
            String originUnLocode,
            String destinationUnLocode) {
        return graphTraversalResource
                .queryParam("origin", originUnLocode)
                .queryParam("destination", destinationUnLocode)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(new GenericType<List<TransitPath>>() {
                });
    }
}
