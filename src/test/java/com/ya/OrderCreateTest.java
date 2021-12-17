package com.ya;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private Order order;
    private OrderClient orderClient;
    private List <ScooterColor> color;
    private int trackID;
    @Before
    public void setUp() {
        order = Order.getRandomOrder(color);
        orderClient = new OrderClient();
    }

    public OrderCreateTest(List<ScooterColor> color){
        this.color = color;

    }
    @Parameterized.Parameters
    public static Object[][] getTestData(){
        return new Object[][]{
                {List.of(ScooterColor.BLACK)},
                {List.of(ScooterColor.GREY)},
                {List.of(ScooterColor.BLACK, ScooterColor.GREY)},
                {null}
        };
    }
    @Test
    public void orderCanBeCreated() {
        ValidatableResponse response = orderClient.createOrder(order);
        int statusCode = response.extract().statusCode();
        trackID = response.extract().path("track");


        assertEquals("Некорректный код статуса", 201, statusCode);
        assertThat("Некорректный ID трека", trackID, notNullValue());
    }
}
