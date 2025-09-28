package api.users;

import api.base.BaseTest;
import api.clients.UserClient;
import api.models.User;
import api.utils.TestData;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

@DisplayName("Изменение данных пользователя")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class UserUpdateTest extends BaseTest {
    private final UserClient userClient = new UserClient();
    private String token;
    private User initialUser;

    @BeforeEach
    void setUp() {
        initialUser = new User(TestData.uniqueEmail(), TestData.password(), TestData.name());
        Response response = userClient.register(initialUser);
        token = response.jsonPath().getString("accessToken");
    }

    @AfterEach
    void tearDown() {
        if (token != null && !token.isEmpty()) {
            userClient.deleteUser(token);
            token = null; // сбрасываем, чтобы не потянулось в другой тест
        }
    }

    // ---------- Авторизованный ----------

    @Test
    @DisplayName("1. Авторизованный пользователь может изменить email")
    void patch_withAuth_200_changeEmail() {
        User updated = new User(TestData.uniqueEmail(), initialUser.getPassword(), initialUser.getName());
        Response response = userClient.updateUser(token, updated);

        Assertions.assertEquals(200, response.statusCode(), "Код ответа должен быть 200");
        Assertions.assertTrue(response.asString().contains(updated.getEmail()), "Email должен измениться");
    }

    @Test
    @DisplayName("2. Авторизованный пользователь может изменить name")
    void patch_withAuth_200_changeName() {
        User updated = new User(initialUser.getEmail(), initialUser.getPassword(), "NewName");
        Response response = userClient.updateUser(token, updated);

        Assertions.assertEquals(200, response.statusCode(), "Код ответа должен быть 200");
        Assertions.assertTrue(response.asString().contains("NewName"), "Имя должно измениться на NewName");
    }

    @Test
    @DisplayName("3. Авторизованный пользователь может изменить password")
    void patch_withAuth_200_changePassword() {
        User updated = new User(initialUser.getEmail(), "newPassword123", initialUser.getName());
        Response response = userClient.updateUser(token, updated);

        Assertions.assertEquals(200, response.statusCode(), "Код ответа должен быть 200");
        Assertions.assertTrue(response.asString().contains("success"), "Ответ должен содержать success");
    }

    // ---------- Неавторизованный ----------

    @Test
    @DisplayName("4. Неавторизованный пользователь не может изменить email (401)")
    void patch_noAuth_401_changeEmail() {
        User updated = new User(TestData.uniqueEmail(), initialUser.getPassword(), initialUser.getName());
        Response response = userClient.updateUser("", updated);

        Assertions.assertEquals(401, response.statusCode(), "Код ответа должен быть 401");
        Assertions.assertTrue(response.asString().contains("You should be authorised"));
    }

    @Test
    @DisplayName("5. Неавторизованный пользователь не может изменить name (401)")
    void patch_noAuth_401_changeName() {
        User updated = new User(initialUser.getEmail(), initialUser.getPassword(), "NoAuthName");
        Response response = userClient.updateUser("", updated);

        Assertions.assertEquals(401, response.statusCode(), "Код ответа должен быть 401");
        Assertions.assertTrue(response.asString().contains("You should be authorised"));
    }

    @Test
    @DisplayName("6. Неавторизованный пользователь не может изменить password (401)")
    void patch_noAuth_401_changePassword() {
        User updated = new User(initialUser.getEmail(), "unauthPass", initialUser.getName());
        Response response = userClient.updateUser("", updated);

        Assertions.assertEquals(401, response.statusCode(), "Код ответа должен быть 401");
        Assertions.assertTrue(response.asString().contains("You should be authorised"));
    }
}
