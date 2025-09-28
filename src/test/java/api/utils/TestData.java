package api.utils;

import io.restassured.path.json.JsonPath;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class TestData {
    public static String uniqueEmail() {
        return "user_" + UUID.randomUUID() + "@test.com";
    }

    public static String password() {
        return "password123";
    }

    public static String name() {
        return "TestUser";
    }

    public static List<String> validIngredients() {
        String response = given()
                .then()
                .statusCode(200)

        JsonPath json = new JsonPath(response);
        List<String> ingredients = json.getList("data._id");

        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalStateException("Сервер не вернул ингредиенты!");
        }

        // Возвращаем 1–2 случайных айдишника
        return ingredients.subList(0, Math.min(2, ingredients.size()));
    }

    public static List<String> invalidIngredients() {
        return Collections.singletonList("invalid-hash-" + UUID.randomUUID());
    }
}
