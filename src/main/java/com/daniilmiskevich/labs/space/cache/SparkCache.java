package com.daniilmiskevich.labs.space.cache;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.daniilmiskevich.labs.space.model.Spark;

@Component
public class SparkCache {
    private static record CacheEntry(String namePattern, Set<String> spectreNames) {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(SparkCache.class);


    private final Map<CacheEntry, List<Spark>> cache;

    public SparkCache(@Value("${labs.cache.max-size}") int maxSize) {
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<CacheEntry, List<Spark>> eldest) {
                var shouldInvalidate = size() > maxSize;

                if (LOGGER.isInfoEnabled() && shouldInvalidate) {
                    LOGGER.info("Invalidated: {}", eldest.getKey());
                }

                return shouldInvalidate;
            }
        };
    }

    public List<Spark> putByNamePatternAndSpectreNames(
        String namePattern,
        Set<String> spectreNames,
        List<Spark> value) {
        var entry = new CacheEntry(namePattern, spectreNames);

        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Put into cache: {}", entry);
        }

        return cache.put(entry, value);

    }

    public List<Spark> getByNamePatternAndSpectreNames(
        String namePattern,
        Set<String> spectreNames) {
        var entry = new CacheEntry(namePattern, spectreNames);

        var value = cache.get(entry);

        if (LOGGER.isInfoEnabled()) {
            if (value != null) {
                LOGGER.info("Taken from cache: {}", entry);
            } else {
                LOGGER.info("Cache miss!");
            }
        }

        return value;
    }

    public void invalidateByNameAndSpectreNames(String name, Set<String> spectreNames) {
        cache.keySet().removeIf(entry -> {
            var regexpNamePattern = String.format("*%s*", entry.namePattern).replace("*", ".*");
            var shouldInvalidate = name.matches(regexpNamePattern)
                || spectreNames.stream().anyMatch(entry.spectreNames::contains);

            if (LOGGER.isInfoEnabled() && shouldInvalidate) {
                LOGGER.info("Invalidated: {}", entry);
            }

            return shouldInvalidate;
        });
    }

}
