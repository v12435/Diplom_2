package api.users;

import api.base.BaseTest;
import api.clients.UserClient;
import api.models.Credentials;
import api.models.User;
import api.utils.TestData;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

@DisplayName("Логин пользователя")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class UserLoginTest extends BaseTest {
    private final UserClient userClient = new UserClient();
    private String token;
    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        email = TestData.uniqueEmail();
        password = TestData.password();
        User u = new User(email, password, TestData.name());
        Response r = userClient.register(u);
        token = r.jsonPath().getString("accessToken");
    }

    @AfterEach
    void tearDown() {
        if (token != null && !token.isEmpty()) {
            userClient.deleteUser(token);
        }
    }

    @Test
    @DisplayName("1. Успешный логин с существующим пользователем")
    void login_success_200_hasAccessToken() {
        Credentials creds = new Credentials(email, password);
        Response r = userClient.login(creds);

        Assertions.assertEquals(200, r.statusCode(), "Код ответа должен быть 200");
        Assertions.assertTrue(r.asString().contains("accessToken"), "Должен вернуться accessToken");
    }

    @Test
    @DisplayName("2. Ошибка логина с неверными данными (401)")
    void login_wrongCreds_401() {
        Credentials creds = new Credentials("wrong@mail.com", "wrongpass");
        Response r = userClient.login(creds);

        Assertions.assertEquals(401, r.statusCode(), "Код ответа должен быть 401");
        Assertions.assertTrue(r.asString().contains("email or password are incorrect"),
                "Ответ должен содержать сообщение \"email or password are incorrect\"");
    }
}
