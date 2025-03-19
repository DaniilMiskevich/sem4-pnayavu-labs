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

    private final Logger logger = LoggerFactory.getLogger(SparkCache.class);


    private final Map<CacheEntry, List<Spark>> cache;

    public SparkCache(@Value("${labs.cache.max-size}") int maxSize) {
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<CacheEntry, List<Spark>> eldest) {
                var shouldInvalidate = size() > maxSize;

                if (shouldInvalidate) {
                    logger.info(String.format("Invalidated: %s", eldest.getKey().toString()));
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

        logger.info(String.format("Put into cache: %s", entry.toString()));

        return cache.put(entry, value);

    }

    public List<Spark> getByNamePatternAndSpectreNames(
        String namePattern,
        Set<String> spectreNames) {
        var entry = new CacheEntry(namePattern, spectreNames);

        var value = cache.get(entry);

        logger.info(value == null
            ? "Cache miss!"
            : String.format("Taken from cache: %s", entry.toString()));

        return value;
    }

    public void invalidateByNameAndSpectreNames(String name, Set<String> spectreNames) {
        cache.keySet().removeIf(entry -> {
            var regexpNamePattern = String.format("*%s*", entry.namePattern).replace("*", ".*");
            var shouldInvalidate = name.matches(regexpNamePattern)
                || spectreNames.stream().anyMatch(entry.spectreNames::contains);

            if (shouldInvalidate) {
                logger.info(String.format("Invalidated: %s", entry.toString()));
            }

            return shouldInvalidate;
        });
    }

}
