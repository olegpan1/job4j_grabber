package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";

    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

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
        for (int i = 1; i < 6; i++) {
            Document document = new Document(PAGE_LINK);
            try {
                document = Jsoup.connect(PAGE_LINK + i).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements rows = document.select(".vacancy-card__inner");
            rows.forEach(row -> {
                Element titleElement = row.select(".vacancy-card__title").first();
                Element linkElement = titleElement.child(0);
                String vacancyName = titleElement.text();
                String date = row.select(".vacancy-card__date")
                        .first().child(0).attr("datetime").split("T")[0];
                String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
                String vacancyDescription = new HabrCareerParse().retrieveDescription(link);
                System.out.printf("%s %s%n %s%n %s%n%n", date, vacancyName, vacancyDescription, link);
            });
        }
    }
}