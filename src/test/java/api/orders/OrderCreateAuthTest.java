package api.orders;

import api.base.BaseTest;
import api.clients.OrderClient;
import api.clients.UserClient;
import api.models.OrderRequest;
import api.models.User;
import api.utils.TestData;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

@DisplayName("Создание заказов (авторизованный пользователь)")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class OrderCreateAuthTest extends BaseTest {
    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();
    private String token;

    @BeforeEach
    void setUp() {
        User u = new User(TestData.uniqueEmail(), TestData.password(), TestData.name());
        Response response = userClient.register(u);
        token = response.jsonPath().getString("accessToken");
    }

    @AfterEach
    void tearDown() {
        if (token != null && !token.isEmpty()) {
            userClient.deleteUser(token);
        }
    }

    @Test
    @DisplayName("1. Создание заказа с авторизацией и ингредиентами (200)")
    void createOrder_auth_withIngredients_200() {
        OrderRequest order = new OrderRequest(TestData.validIngredients());
        Response response = orderClient.createOrderAuth(token, order);

        Assertions.assertEquals(200, response.statusCode(),
                "Код ответа должен быть 200");
        Assertions.assertTrue(response.asString().contains("success"),
                "Ответ должен содержать success");
    }
}
