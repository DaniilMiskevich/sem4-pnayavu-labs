package com.daniilmiskevich.labs.space.cache;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.daniilmiskevich.labs.space.model.Spark;

@Component
public class SparkCache {
    private final Map<CacheEntry, List<Spark>> cache;

    public SparkCache(@Value("${labs.cache.max-size}") int maxSize) {
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<CacheEntry, List<Spark>> eldest) {
                return size() > maxSize;
            }
        };
    }

    public void putByNamePatternAndSpectreNames(
        String namePattern,
        Set<String> spectreNames,
        List<Spark> value) {
        cache.put(new CacheEntry(namePattern, spectreNames), value);
    }

    public List<Spark> getByNamePatternAndSpectreNames(
        String namePattern,
        Set<String> spectreNames) {
        var entry = new CacheEntry(namePattern, spectreNames);

        return cache.get(entry);
    }

    public void invalidateByNameAndSpectreNames(String name, Set<String> spectreNames) {
        cache.keySet().removeIf(entry -> {
            var regexpNamePattern = String.format("*%s*", entry.namePattern).replace("*", ".*");
            return name.matches(regexpNamePattern)
                || spectreNames.stream().anyMatch(entry.spectreNames::contains);
        });
    }

    private record CacheEntry(String namePattern, Set<String> spectreNames) {
    }

}
