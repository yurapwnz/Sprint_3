package com.ya;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderListTest {
    private OrderClient orderClient;
    @Before
    public void setUp(){
        orderClient=new OrderClient();
    }
    @Test
    public void checkListOfOrderTest(){
        ValidatableResponse response = orderClient.getOrderList();
        int statusCode = response.extract().statusCode();
        List<Map<String, Object>> orders = response.extract().jsonPath().getList("orders");

        assertThat("Status code is incorrect", statusCode, equalTo(200));
        assertThat("List is empty",orders,notNullValue());
    }
}
