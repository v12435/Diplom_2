package api.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = Config.class
                .getClassLoader()
                .getResourceAsStream("test.properties")) {
            if (input == null) {
                throw new RuntimeException("Не найден файл test.properties");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки test.properties", e);
        }
    }

    public static String getBaseUrl() {
        return System.getProperty("baseUrl", props.getProperty("baseUrl"));
    }
}
