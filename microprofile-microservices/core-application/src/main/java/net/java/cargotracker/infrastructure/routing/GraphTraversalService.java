package net.java.cargotracker.infrastructure.routing;

import java.util.*;
import javax.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "http://localhost:8888/path-finder/rest")
@Path("/graph-traversal")
public interface GraphTraversalService {

    @GET
    @Path("/shortest-path")
    @Produces("application/json")
    public List<TransitPath> findShortestPath(
            @QueryParam("origin") String originUnLocode,
            @QueryParam("destination") String destinationUnLocode);
}
