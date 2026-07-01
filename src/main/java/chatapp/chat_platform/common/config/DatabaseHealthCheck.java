package chatapp.chat_platform.common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseHealthCheck implements CommandLineRunner {

    private final DataSource dataSource;

    @Override
    public void run(String... args) {
        try (Connection connection = dataSource.getConnection()) {
            log.info("Database connected successfully: {}", connection.getMetaData().getURL());
        } catch (Exception e) {
            log.error("Failed to connect to database: {}", e.getMessage());
        }
    }
}
