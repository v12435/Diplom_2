package api.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        // Заменяем все фильтры на один AllureRestAssured,
        // чтобы не было дублей в отчётах
        RestAssured.replaceFiltersWith(new AllureRestAssured());
    }
}
