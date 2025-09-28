package api.utils;

import io.qameta.allure.Allure;

import java.nio.charset.StandardCharsets;

public class AllureUtils {

    private static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();

    private AllureUtils() {
        // утилитный класс — запретим создание экземпляров
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(
                name,
                "text/plain",
                content == null ? "" : content,
                DEFAULT_ENCODING
        );
    }

    public static void attachJson(String name, String json) {
        Allure.addAttachment(
                name,
                "application/json",
                json == null ? "" : json,
                DEFAULT_ENCODING
        );
    }
}
