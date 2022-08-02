package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.Post;
import ru.job4j.utils.DateTimeParser;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    private final DateTimeParser dateTimeParser;

    private List<Post> postList;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) {
        SqlRuParse sqlRuParse = new SqlRuParse(new SqlRuDateTimeParser());
        sqlRuParse.postList = sqlRuParse.list("https://www.sql.ru/forum/job-offers");
        for (Post post : sqlRuParse.postList) {
            System.out.println(post);
            System.out.println();
        }
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

    public Post detail(String link) {
        Element parent = loadPostDetails(link);
        return new Post(0,
                parent.child(0).child(0).text(),
                link, parent.child(1).child(1).text(),
                dateTimeParser.parse(parent.child(2).child(0).text()));
    }

    public Element loadPostDetails(String link) {
        Document doc = new Document(link);
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc.select(".msgTable").get(0).child(0);
    }
}
