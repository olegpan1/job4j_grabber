package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.Post;
import ru.job4j.utils.DateTimeParser;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd MMMM yy, HH:mm");

    private final DateTimeParser dateTimeParser;

    private List<Post> postList;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> resultList = new ArrayList<>();
        String postLink;
        Elements row = getElements(link);
        for (Element td : row) {
            if (!td.text().contains("Важно:")) {
                postLink = td.child(0).attr("href");
                resultList.add(detail(postLink));
            }
        }
        return resultList;
    }

    private Elements getElements(String link) {
        Elements elements = new Elements();
        try {
            elements = Jsoup.connect(link).get().select(".postslisttopic");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return elements;
    }

    @Override
    public Post detail(String link) {
        return new Post().getPost(link);
    }

    public static void main(String[] args) {
        SqlRuParse sqlRuParse = new SqlRuParse(new SqlRuDateTimeParser());
        sqlRuParse.postList = sqlRuParse.list("https://www.sql.ru/forum/job-offers");
        for (Post post : sqlRuParse.postList) {
            System.out.println(post.getTitle());
            System.out.println(post.getLink());
            System.out.println();
        }
    }
}
