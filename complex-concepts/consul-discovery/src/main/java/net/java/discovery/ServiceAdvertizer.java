package net.java.discovery;

import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.State;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

public class ServiceAdvertizer {

	public void advertize(ServiceAdvertisement advertisement) {
		advertize(advertisement, Executors.newSingleThreadScheduledExecutor());
	}

	public void advertize(ServiceAdvertisement advertisement, ScheduledExecutorService scheduler) {
		Config config = ConfigProvider.getConfig();
		String serviceAddress = getAddressFromAdvert(advertisement, config);
		Integer servicePort = getPortFromAdvert(advertisement, config);
		Registration service = ImmutableRegistration.builder().id(idFromAdvert(advertisement))
				.name(advertisement.getName()).address(serviceAddress).port(servicePort)
				.check(Registration.RegCheck.ttl(3L)) // registers with a TTL of 3 seconds
				.meta(Collections.singletonMap(ConsulClient.KEY_CTX_ROOT, advertisement.getContextRoot())).build();
		try {
			ConsulClient.build().agentClient().register(service);
		} catch (RuntimeException e) {
			Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Is consul running?", e);
		}
		// keep refreshing the status within the TTL
		scheduler.scheduleAtFixedRate(() -> refreshAdvertisement(advertisement), 2, 2, TimeUnit.SECONDS);
	}

	public void refreshAdvertisement(ServiceAdvertisement advertisement) {
		try {
			Logger.getLogger(ServiceAdvertizer.class.getName()).log(Level.SEVERE,
					"Sending ping to consul");
			try {
				ConsulClient.build().agentClient().checkTtl(idFromAdvert(advertisement), State.PASS, null);
			} catch (RuntimeException e) {
				Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Is consul running?", e);
			}
		} catch (NotRegisteredException ex) {
			Logger.getLogger(ServiceAdvertizer.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
	}

	public void stopAdvertizing(ServiceAdvertisement advertisement) {
		ConsulClient.build().agentClient().deregister(idFromAdvert(advertisement));
	}

	private String idFromAdvert(ServiceAdvertisement advertisement) {
		Config config = ConfigProvider.getConfig();
		String serviceAddress = getAddressFromAdvert(advertisement, config);
		Integer servicePort = getPortFromAdvert(advertisement, config);
		return advertisement.getName() + ":" + serviceAddress + ":" + servicePort;
	}

	private static Integer getPortFromAdvert(ServiceAdvertisement advertisment, Config config) {
		Integer servicePort = advertisment.getPort();
		if (servicePort == null) {
			servicePort = ServiceInfo.getServicePort(config);
		}
		return servicePort;
	}

	private static String getAddressFromAdvert(ServiceAdvertisement advertisment, Config config) {
		String serviceAddress = advertisment.getAddress();
		if (serviceAddress == null) {
			serviceAddress = ServiceInfo.getServiceAddress(config);
		}
		return serviceAddress;
	}

}
