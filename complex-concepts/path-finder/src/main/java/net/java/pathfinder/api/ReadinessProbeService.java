package net.java.pathfinder.api;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("ready")
public class ReadinessProbeService {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String ready() {
        return "ready";
    }
}
