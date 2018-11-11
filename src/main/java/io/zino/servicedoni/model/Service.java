package io.zino.servicedoni.model;

import java.net.URI;
import java.util.Objects;

public class Service {
    private long ttl;
    private long registerationTime;
    private String name;
    private URI uri;

    public long getExpiration() {
        return registerationTime + ttl;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public long getRegisterationTime() {
        return registerationTime;
    }

    public void setRegisterationTime(long registerationTime) {
        this.registerationTime = registerationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(name, service.name) &&
                Objects.equals(uri, service.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, uri);
    }
}
