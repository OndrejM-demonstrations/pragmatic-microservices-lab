package net.java.discovery;

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

public class SelfHealthService {
    
    /**
     * Uses MicroProfile health endpoint to get health status
     * @return
     */
    public boolean isInHealthyState() {
        Config config = ConfigProvider.getConfig();
        URL target;
        try {
            target = config.getOptionalValue("health.url", URL.class)
                    .orElse(new URL("http://localhost:" + ServiceInfo.getServicePort(config)));
            Response healthResponse = ClientBuilder.newClient().target(target.toURI()).path("health").request().get();
            return Response.Status.OK.getStatusCode() == healthResponse.getStatus();
        } catch (MalformedURLException | URISyntaxException ex) {
            Logger.getLogger(SelfHealthService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
