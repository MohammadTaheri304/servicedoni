package io.zino.servicedoni.serviceregistery;

import com.google.gson.Gson;
import io.zino.servicedoni.model.Service;
import io.zino.servicedoni.model.request.RegisterRequest;
import io.zino.servicedoni.model.response.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ServiceRegisteryEngine {
    private final static Gson gson = new Gson();
    private static Logger logger = LoggerFactory.getLogger(ServiceRegisteryEngine.class);
    private final static ServiceRegisteryEngine instance = new ServiceRegisteryEngine();

    private final ConcurrentMap<String, Map<URI, Service>> availableServices = new ConcurrentHashMap<>();

    public static ServiceRegisteryEngine getInstance() {
        return instance;
    }

    private ServiceRegisteryEngine() {
        this.startExpireationTask();
        logger.debug("ServiceRegisteryEngine started!");
    }

    public void register(RegisterRequest registerRequest) {
        Map<URI, Service> services = availableServices.computeIfAbsent(
                registerRequest.getName(),
                insertedKey -> new HashMap<>());
        Service newService = new Service();
        newService.setName(registerRequest.getName());
        newService.setRegisterationTime(new Date().getTime());
        newService.setTtl(registerRequest.getTtl());
        newService.setUri(registerRequest.getUri());
        synchronized (services) {
            Service service = services.get(newService.getUri());
            if (service != null) {
                long newTTL = newService.getTtl() + new Date().getTime() - service.getRegisterationTime();
                service.setTtl(newTTL);
                logger.debug("service refreshed: {}", gson.toJson(registerRequest));
            } else {
                services.put(newService.getUri(), newService);
                logger.info("service registered: {}", gson.toJson(registerRequest));
            }
        }
    }

    public ServiceResponse getServiceByName(String serviceName) {
        logger.debug("ask for service:{}", serviceName);
        Map<URI, Service> serviceMap = availableServices.computeIfAbsent(
                serviceName,
                insertedKey -> new HashMap<>());
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setServices(
                serviceMap.entrySet().stream()
                        .map(pair -> pair.getValue())
                        .collect(Collectors.toSet())
        );
        logger.debug("getService response for name {}: {}", serviceName, gson.toJson(serviceResponse));
        return serviceResponse;
    }

    private void startExpireationTask() {
        TimerTask task = new TimerTask() {
            public void run() {
                ServiceRegisteryEngine.this.expireationTask();
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 1000L;
        long period = 1000L;
        timer.schedule(task, delay, period);
        logger.debug("startExpireationTask method finished succ");
    }

    private void expireationTask() {
        logger.debug("expireationTask run!");
        for (Map<URI, Service> services : availableServices.values()) {
            synchronized (services) {
                for (Service service : services.values()) {
                    logger.debug("expireationTask check for service {}", gson.toJson(service));
                    if (new Date().getTime() > service.getExpiration()) {
                        services.remove(service.getUri());
                        logger.info("Service removed : {}", gson.toJson(service));
                    }
                }
            }
        }
    }
}
