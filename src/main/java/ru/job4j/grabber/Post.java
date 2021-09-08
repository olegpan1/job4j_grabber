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

    public Post() {
    }

    public Post(int id, String title, String link, String description, LocalDateTime created) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated() {
        return created;
    }


    public Elements loadPostDetails(String post) {
        Document doc = new Document(post);
        try {
            doc = Jsoup.connect(post).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc.select(".msgTable");
    }

    public Post getPost(String link) {
        Elements row = loadPostDetails(link);
        Element parent = row.get(0).child(0);
        id = (Integer.parseInt(parent.child(0).child(0).attr("id").substring(2)));
        title = parent.child(0).child(0).text();
        description = parent.child(1).child(1).text();
        created = new SqlRuDateTimeParser().parse(parent.child(2).child(0).text());
        return new Post(id, title, link, description, created);
    }

    public static void main(String[] args) {
        System.out.println(new Post().getPost(
                "https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t"));

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
        return "Post{" + s
                + "id = " + id + s
                + "title = " + title + s
                + "link = " + link + s
                + "description = " + description + s
                + "created = " + FORMATTER.format(created) + s
                + '}';
    }
}
