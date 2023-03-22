package ru.practicum.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.practicum.client.event.statclient.StatisticClient;

@SpringBootApplication(scanBasePackageClasses = {StatisticClient.class})
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

}
