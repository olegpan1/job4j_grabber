package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Post {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd MMMM yy, HH:mm");

    private int id;
    private String title;
    private String link;
    private String description;
    private LocalDateTime created;


    public Elements loadPostDetails(String post) throws IOException {
        Document doc = Jsoup.connect(post).get();
        return doc.select(".msgTable");
    }

    public static void main(String[] args) throws IOException {
        Elements row = new Post().loadPostDetails(
                "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t");
        for (Element parent : row) {
            System.out.println(parent.child(0).child(1).child(1).text());
            System.out.println(FORMATTER.format(new SqlRuDateTimeParser()
                    .parse(parent.child(0).child(2).child(0).text())));
            System.out.println();
        }
    }

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
                && Objects.equals(created, post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, link, created);
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
