package io.zino.servicedoni.model.response;

import io.zino.servicedoni.model.Service;

import java.util.Set;

public class ServiceResponse {
    private Set<Service> services;

    public Set<Service> getServices() {
        return services;
    }

    public void setServices(Set<Service> services) {
        this.services = services;
    }
}
