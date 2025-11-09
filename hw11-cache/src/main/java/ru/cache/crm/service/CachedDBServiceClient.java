package ru.cache.crm.service;

import java.util.List;
import java.util.Optional;
import ru.cache.cachehw.HwCache;
import ru.cache.crm.model.Client;

public class CachedDBServiceClient implements DBServiceClient {
    private final DBServiceClient delegate;
    private final HwCache<Long, Client> cache;

    public CachedDBServiceClient(DBServiceClient delegate, HwCache<Long, Client> cache) {
        this.delegate = delegate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        Client savedClient = delegate.saveClient(client);

        if (savedClient != null && savedClient.getId() != null) {
            cache.put(savedClient.getId(), savedClient);
        }
        return savedClient;
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = cache.get(id);
        if (cachedClient != null) {
            return Optional.of(cachedClient);
        }

        Optional<Client> clientOptional = delegate.getClient(id);
        clientOptional.ifPresent(client -> cache.put(client.getId(), client));

        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        return delegate.findAll();
    }
}
