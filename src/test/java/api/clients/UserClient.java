package api.clients;

import api.models.User;
import api.models.Credentials;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String BASE_PATH = "/api/auth";

    @Step("Регистрируем пользователя {user.email}")
    public Response register(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(BASE_PATH + "/register")
                .then().extract().response();
    }

    @Step("Логинимся пользователем {creds.email}")
    public Response login(Credentials creds) {
        return given()
                .header("Content-type", "application/json")
                .body(creds)
                .post(BASE_PATH + "/login")
                .then().extract().response();
    }

    @Step("Обновляем пользователя по токену {token}")
    public Response updateUser(String token, User user) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(user)
                .patch(BASE_PATH + "/user")
                .then().extract().response();
    }

    @Step("Удаляем пользователя по токену {token}")
    public Response deleteUser(String token) {
        return given()
                .header("Authorization", token)
                .delete(BASE_PATH + "/user")
                .then().extract().response();
    }
}
