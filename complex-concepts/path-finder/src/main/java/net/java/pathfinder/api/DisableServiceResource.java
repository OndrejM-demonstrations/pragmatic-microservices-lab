/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.pathfinder.api;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import net.java.pathfinder.health.ServiceDisabledHealthCheck;
import org.eclipse.microprofile.health.Health;

/**
 * REST Web Service
 *
 * @author Ondro Mihalyi
 */
@Path("disable")
@RequestScoped
public class DisableServiceResource {

    @Inject
    @Health
    ServiceDisabledHealthCheck healthCheck;
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String disable() {
        healthCheck.disable();
        return "Service is disabled from now";
    }

}
