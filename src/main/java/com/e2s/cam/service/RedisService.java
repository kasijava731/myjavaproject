package com.e2s.cam.service;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class RedisService {
    private final Jedis jedis;
    public RedisService() {
        // Initialize Jedis with the Redis server configuration
        jedis = new Jedis("localhost", 6379);
        // You can also set authentication and other configurations here if needed
    }

    public void saveData(String key, String value) {
        jedis.set(key, value);
    }

    public String getData(String key) {
        return jedis.get(key);
    }

    public void deleteData(String key) {
        jedis.del(key);
    }
    public void setValue(String key, String value, int expiry){
        
            jedis.set(key, value);
            jedis.expire(key, expiry);
      
    }
}
