package ru.job4j.cache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class DirFileCache extends AbstractCache<String, String> {

    private final String cachingDir;
    public final String separator = System.lineSeparator();

    public DirFileCache(String cachingDir) {
        this.cachingDir = cachingDir;
    }

    public String getCachingDir() {
        return cachingDir;
    }

    @Override
    protected String load(String key) {
        StringJoiner text = new StringJoiner(separator);
        try (Stream<String> stream = Files.lines(new File(cachingDir, key).toPath())) {
            stream.forEach(text::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
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