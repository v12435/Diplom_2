package api.clients;

import api.models.OrderRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_PATH = "/api/orders";

    @Step("Получаем список ингредиентов")
    public Response getIngredients() {
        return given()
                .get("/api/ingredients")
                .then().extract().response();
    }

    @Step("Создаём заказ с авторизацией")
    public Response createOrderAuth(String token, OrderRequest order) {
        return given()
                .header("Authorization", token)
                .body(order)
                .post(BASE_PATH)
                .then().extract().response();
    }

    @Step("Создаём заказ без авторизации")
    public Response createOrderNoAuth(OrderRequest order) {
        return given()
                .body(order)
                .post(BASE_PATH)
                .then().extract().response();
    }

    @Step("Получаем заказы авторизованного пользователя")
    public Response getOrdersAuth(String token) {
        return given()
                .header("Authorization", token)
                .get(BASE_PATH)
                .then().extract().response();
    }

    @Step("Получаем заказы без авторизации")
    public Response getOrdersNoAuth() {
        return given()
                .get(BASE_PATH)
                .then().extract().response();
    }
}
