package ru.job4j.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class DirFileCache extends AbstractCache<String, String> {

    private final String cachingDir;

    public DirFileCache(String cachingDir) {
        this.cachingDir = cachingDir;
    }

    public String getCachingDir() {
        return cachingDir;
    }

    @Override
    protected String load(String key) {
        String fileContent = null;
        try {
            fileContent = Files.readString(Path.of(cachingDir, key));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DirFileCache that = (DirFileCache) o;
        return Objects.equals(cachingDir, that.cachingDir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cachingDir);
    }
}