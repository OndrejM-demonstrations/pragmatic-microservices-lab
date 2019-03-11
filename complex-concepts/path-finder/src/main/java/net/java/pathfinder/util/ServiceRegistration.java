package net.java.pathfinder.util;

import fish.payara.micro.PayaraMicro;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.*;
import javax.enterprise.event.Observes;
import javax.servlet.ServletContext;
import net.java.discovery.ServiceAdvertisement;
import net.java.discovery.ServiceAdvertizer;
import org.eclipse.microprofile.config.ConfigProvider;

@ApplicationScoped
public class ServiceRegistration {
    
    @Resource
    private ManagedScheduledExecutorService scheduler;
    
    private ServiceAdvertisement advert;
    
    private ServiceAdvertizer advertizer;
    
    @PostConstruct
    public void init() {
        advertizer = new ServiceAdvertizer();
    }

    public void registerAtStartup(@Observes @Initialized(ApplicationScoped.class) ServletContext context) {
        int port = PayaraMicro.getInstance().getRuntime().getLocalDescriptor().getHttpPorts().get(0);
        advert = new ServiceAdvertisement()
                .withName("pathfinder")
                .withPort(port)
                .withContextRoot(context.getContextPath());
        try {
        	advertizer.advertize(advert, scheduler);
        } catch (RuntimeException e) {
        	Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Is consul running?", e);
        }
        
        scheduler.scheduleAtFixedRate(() -> {
            Logger.getLogger(this.getClass().getName()).info(() -> {
                String url = ConfigProvider.getConfig().getValue("discovery.service.pathfinder.url", String.class);
                return "URL of a healthy pathfinder service: " + url;
            });
        }, 1, 5, TimeUnit.SECONDS);
    }

    public void deregisterAtShutdown(@Observes @BeforeDestroyed(ApplicationScoped.class) Object destroy) {
        advertizer.stopAdvertizing(advert);
    }
}
