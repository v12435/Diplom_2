package api.base;

import api.utils.Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseTest {

    @BeforeAll
    static void setup() {
        // Берём url либо из System property (-Dbase.url=...), либо из test.properties
        RestAssured.baseURI = Config.getBaseUrl();

        // Заменяем все фильтры на один AllureRestAssured,
        // чтобы не было дублей в отчётах
        RestAssured.replaceFiltersWith(new AllureRestAssured());
    }
}
