package cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class GuavaDemo implements Serializable {
    public static void main(String[] args) {
        Cache<String, String> cache = CacheBuilder.newBuilder()
                .maximumSize(1024)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .weakValues()
                .build();

        cache.put("key", "Hello Guava Cache");
        System.out.println(cache.getIfPresent("key"));
    }
}
