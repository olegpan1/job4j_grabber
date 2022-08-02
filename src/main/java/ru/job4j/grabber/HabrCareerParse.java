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
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> resultList = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Elements rows = connect(link, i);
            resultList.addAll(getVacancy(resultList, rows));
        }
        return resultList;
    }

    private Elements connect(String link, int i) {
        Document document = new Document(link);
        try {
            document = Jsoup.connect(link + i).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document.select(".vacancy-card__inner");
    }

    private List<Post> getVacancy(List<Post> resultList, Elements rows) {
        rows.forEach(row -> {
            Element titleElement = row.select(".vacancy-card__title").first();
            Element linkElement = titleElement.child(0);
            String vacancyName = titleElement.text();
            String date = row.select(".vacancy-card__date")
                    .first().child(0).attr("datetime");
            String vacancyLink = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
            String vacancyDescription = retrieveDescription(vacancyLink);
            resultList.add(new Post(vacancyName, vacancyLink, vacancyDescription, dateTimeParser.parse(date)));
        });
        return resultList;
    }

    private String retrieveDescription(String link) {
        Document doc = new Document(link);
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
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