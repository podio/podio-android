package com.podio.sdk.client.cache;

public interface CacheClient {

    byte[] load(String key);

    void save(String key, byte[] data);

    boolean delete(String key);
}
