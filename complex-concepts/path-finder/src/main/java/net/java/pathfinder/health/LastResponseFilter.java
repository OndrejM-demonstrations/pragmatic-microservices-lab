package net.java.pathfinder.health;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Ondrej Mihalyi
 */
@Provider
@ApplicationScoped
public class LastResponseFilter implements javax.ws.rs.container.ContainerResponseFilter {

    volatile private long lastResponseCompletedTimeMillis = System.currentTimeMillis();
    
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        lastResponseCompletedTimeMillis = System.currentTimeMillis();
    }

    public long getLastResponseCompletedTimeMillis() {
        return lastResponseCompletedTimeMillis;
    }

}

