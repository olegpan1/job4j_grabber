package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


public class AlertRabbit {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private Connection cn;

    public AlertRabbit() {
        initConnection();
    }

    public void initConnection() {
        try (InputStream in = AlertRabbit.class.getClassLoader()
                .getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private Scheduler startScheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    private void addNewJobToScheduler(String name, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put(name, cn);
        JobDetail job = newJob(Rabbit.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(3)
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class Rabbit implements Job {
        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            String  name = context.getJobDetail().getJobDataMap().getKeys()[0];
            Connection cn = (Connection) context.getJobDetail().getJobDataMap().get(name);
            try (PreparedStatement statement = cn.prepareStatement(
                    "insert into rabbit(name, created_date) values (?, ?)")) {
                statement.setString(1, name);
                statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void showResult() {
        try (PreparedStatement statement = cn.prepareStatement(
                "select  * from rabbit;")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("Result from table rabbit");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("name") + " - "
                            + FORMATTER.format(
                                    resultSet.getTimestamp("created_date").toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        AlertRabbit alertRabbit = new AlertRabbit();
        try {
            Scheduler scheduler = alertRabbit.startScheduler();
            alertRabbit.addNewJobToScheduler("connection1", scheduler);
            alertRabbit.addNewJobToScheduler("connection2", scheduler);
            Thread.sleep(10000);
            scheduler.shutdown();
            alertRabbit.showResult();
            alertRabbit.cn.close();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }
}


