package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.utils.DateTimeParser;
import ru.job4j.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HabrCareerParse implements Parse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);
    private static final int NUMBER_OF_PAGES = 5;
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> resultList = new ArrayList<>();
        for (int i = 1; i <= NUMBER_OF_PAGES; i++) {
            Elements rows = connect(link + i);
            rows.forEach(row -> resultList.add(getPost(row)));
        }
        return resultList;
    }

    private Elements connect(String link) {
        Document document;
        try {
            document = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    String.format("Can`t open page %s%n", link));
        }
        return document.select(".vacancy-card__inner");
    }

    private Post getPost(Element row) {
        Element titleElement = row.select(".vacancy-card__title").first();
        String vacancyName = titleElement.text();
        String date = row.select(".vacancy-card__date")
                .first().child(0).attr("datetime");
        String vacancyLink = String.format("%s%s", SOURCE_LINK, titleElement.child(0).attr("href"));
        String vacancyDescription = retrieveDescription(vacancyLink);
        return new Post(vacancyName, vacancyLink, vacancyDescription, dateTimeParser.parse(date));
    }

    private String retrieveDescription(String link) {
        Document doc;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    String.format("Can`t open page %s%n", link));
        }
        return doc.select(".style-ugc").text();
    }

    public static void main(String[] args) {
        HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
        for (Post post : habrCareerParse.list(PAGE_LINK)) {
            System.out.println(post);
        }
    }
}