package api.orders;

import api.base.BaseTest;
import api.clients.OrderClient;
import api.models.OrderRequest;
import api.utils.TestData;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;

@DisplayName("Создание заказов (неавторизованный пользователь)")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class OrderCreateNoAuthTest extends BaseTest {
    private final OrderClient orderClient = new OrderClient();

    @Test
    @DisplayName("1. Получение списка ингредиентов (200)")
    void ingredients_get_200() {
        Response response = orderClient.getIngredients();

        Assertions.assertEquals(200, response.statusCode(),
                "Код ответа должен быть 200");
        Assertions.assertTrue(response.asString().contains("data"),
                "Ответ должен содержать список ингредиентов");
    }

    @Test
    @DisplayName("2. Создание заказа без авторизации, но с ингредиентами (200)")
    void createOrder_noAuth_withIngredients_200() {
        OrderRequest order = new OrderRequest(TestData.validIngredients());
        Response response = orderClient.createOrderNoAuth(order);

        Assertions.assertEquals(200, response.statusCode(),
                "Код ответа должен быть 200 (по текущему API)");
        Assertions.assertTrue(response.asString().contains("success"),
                "Ответ должен содержать success");
    }

    @Test
    @DisplayName("3. Создание заказа без ингредиентов — ошибка 400")
    void createOrder_noIngredients_400() {
        OrderRequest order = new OrderRequest(List.of());
        Response response = orderClient.createOrderNoAuth(order);

        Assertions.assertEquals(400, response.statusCode(),
                "Код ответа должен быть 400");
        Assertions.assertTrue(response.asString().contains("Ingredient ids must be provided"),
                "Ответ должен содержать сообщение Ingredient ids must be provided");
    }

    @Test
    @DisplayName("4. Создание заказа с неверным хешем ингредиентов — ошибка 500 (но сейчас 400)")
    void createOrder_invalidIngredientHash_500() {
        OrderRequest order = new OrderRequest(TestData.invalidIngredients());
        Response response = orderClient.createOrderNoAuth(order);

        Assertions.assertEquals(500, response.statusCode(),
                "Код ответа должен быть 500");
        Assertions.assertTrue(
                response.asString().contains("error")
        );
    }
}
