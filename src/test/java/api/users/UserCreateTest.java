package api.users;

import api.base.BaseTest;
import api.clients.UserClient;
import api.models.User;
import api.utils.TestData;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

@DisplayName("Создание пользователя")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class UserCreateTest extends BaseTest {
    private final UserClient userClient = new UserClient();
    private String token;

    @AfterEach
    void tearDown() {
        if (token != null && !token.isEmpty()) {
            userClient.deleteUser(token);
        }
    }

    @Test
    @DisplayName("1. Успешное создание нового пользователя (200)")
    void createUser_200() {
        User user = new User(TestData.uniqueEmail(), TestData.password(), TestData.name());
        Response response = userClient.register(user);

        Assertions.assertEquals(200, response.getStatusCode(), "Код ответа должен быть 200");
        Assertions.assertTrue(response.asString().contains("success"), "Ответ должен содержать success");

        token = response.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("2. Создание пользователя с существующим email — ошибка 403")
    void createUser_existingEmail_403() {
        User user = new User(TestData.uniqueEmail(), TestData.password(), TestData.name());

        // первая регистрация — сохраняем токен для удаления
        Response firstResponse = userClient.register(user);
        Assertions.assertEquals(200, firstResponse.getStatusCode(), "Первая регистрация должна пройти успешно");
        token = firstResponse.jsonPath().getString("accessToken");

        // повторная регистрация
        Response response = userClient.register(user);
        Assertions.assertEquals(403, response.getStatusCode(), "Код ответа должен быть 403 при повторной регистрации");
        Assertions.assertTrue(response.asString().contains("User already exists"),
                "Ответ должен содержать сообщение 'User already exists'");
    }

    @Test
    @DisplayName("3. Создание пользователя без email — ошибка 403")
    void createUser_noEmail_403() {
        User user = new User("", TestData.password(), TestData.name());
        Response response = userClient.register(user);

        Assertions.assertEquals(403, response.getStatusCode(), "Код ответа должен быть 403");
        Assertions.assertTrue(response.asString().contains("Email, password and name are required fields"),
                "Ответ должен содержать сообщение о необходимости email, password и name");
    }

    @Test
    @DisplayName("4. Создание пользователя без пароля — ошибка 403")
    void createUser_noPassword_403() {
        User user = new User(TestData.uniqueEmail(), "", TestData.name());
        Response response = userClient.register(user);

        Assertions.assertEquals(403, response.getStatusCode(), "Код ответа должен быть 403");
        Assertions.assertTrue(response.asString().contains("Email, password and name are required fields"),
                "Ответ должен содержать сообщение о необходимости email, password и name");
    }

    @Test
    @DisplayName("5. Создание пользователя без имени — ошибка 403")
    void createUser_noName_403() {
        User user = new User(TestData.uniqueEmail(), TestData.password(), "");
        Response response = userClient.register(user);

        Assertions.assertEquals(403, response.getStatusCode(), "Код ответа должен быть 403");
        Assertions.assertTrue(response.asString().contains("Email, password and name are required fields"),
                "Ответ должен содержать сообщение о необходимости email, password и name");
    }
}
