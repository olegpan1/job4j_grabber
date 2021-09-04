package ru.job4j.quartz;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    int id;
    String title;
    String link;
    String description;
    LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id
                && Objects.equals(title, post.title)
                && Objects.equals(link, post.link)
                && Objects.equals(description, post.description)
                && Objects.equals(created, post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, link, description, created);
    }

    @Override
    public String toString() {
        String s = System.lineSeparator();
        return "Post{"
                + "id = " + id + s
                + "title = " + title + s
                + "link = " + link + s
                + "description = " + description + s
                + "created = " + created + s
                + '}';
    }
}
