package api.orders;

import api.base.BaseTest;
import api.clients.OrderClient;
import api.clients.UserClient;
import api.models.User;
import api.utils.TestData;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

@DisplayName("Получение заказов (авторизованный пользователь)")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class OrderGetAuthTest extends BaseTest {
    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();
    private String token;

    @BeforeEach
    void setUp() {
        User u = new User(TestData.uniqueEmail(), TestData.password(), TestData.name());
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
    @DisplayName("1. Авторизованный пользователь получает список заказов (200)")
    void userOrders_authorized_200() {
        Response r = orderClient.getOrdersAuth(token);

        Assertions.assertEquals(200, r.statusCode(),
                "Код ответа должен быть 200");
        Assertions.assertTrue(r.asString().contains("orders"),
                "Ответ должен содержать список заказов");
    }
}
