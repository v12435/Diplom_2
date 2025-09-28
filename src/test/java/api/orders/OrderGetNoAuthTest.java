package api.orders;

import api.base.BaseTest;
import api.clients.OrderClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

@DisplayName("Получение заказов (неавторизованный пользователь)")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class OrderGetNoAuthTest extends BaseTest {
    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("1. Неавторизованный пользователь не может получить список заказов (401)")
    void userOrders_noAuth_401() {
        Response r = orderClient.getOrdersNoAuth();

        Assertions.assertEquals(401, r.statusCode(),
                "Код ответа должен быть 401");
        Assertions.assertTrue(r.asString().contains("You should be authorised"),
                "Ответ должен содержать сообщение You should be authorised");
    }
}
