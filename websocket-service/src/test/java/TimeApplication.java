import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class TimeApplication {

    public static void main(String[] args) {
        log.info(String.valueOf(Instant.now().toEpochMilli()));
    }
}
