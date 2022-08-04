package ru.job4j.grabber;

import ru.job4j.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            cnn = DriverManager.getConnection(cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password")
            );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        String insert = "insert into post(name, text, link, created) values (?, ?, ?, ?) on conflict (link) do nothing;";
        try (PreparedStatement statement =
                     cnn.prepareStatement(insert)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> post = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement("select * from post;");
             ResultSet allPost = statement.executeQuery()) {
            while (allPost.next()) {
                post.add(createPost(allPost));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return post;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement =
                     cnn.prepareStatement("select * from post where id=?;")) {
            statement.setInt(1, id);
            try (ResultSet postById = statement.executeQuery()) {
                if (postById.next()) {
                    post = createPost(postById);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return post;
    }

    private Post createPost(ResultSet postInfo) throws SQLException {
        return new Post(postInfo.getInt("id"),
                postInfo.getString("name"),
                postInfo.getString("link"),
                postInfo.getString("text"),
                postInfo.getTimestamp("created").toLocalDateTime());
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
        Properties config = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("psqlStore.properties")) {
            config.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (PsqlStore psqlStore = new PsqlStore(config)) {
            HabrCareerParse habrCareerParse = new HabrCareerParse(new HabrCareerDateTimeParser());
            for (Post post : habrCareerParse.list("https://career.habr.com/vacancies/java_developer?page=")) {
                psqlStore.save(post);
            }

            List<Post> all = psqlStore.getAll();
            System.out.println("Number of entries in the database of vacancies after parsing: " + all.size());

            System.out.println("Find by ID: " + psqlStore.findById(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}