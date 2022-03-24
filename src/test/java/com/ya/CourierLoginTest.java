package com.ya;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class CourierLoginTest {
    private Courier courier;
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courier = Courier.getRandom();
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }


    @Test
    public void checkCourierCanBeCreated() {
        courierClient.create(courier);
        ValidatableResponse response = courierClient.login(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        int courierId = response.extract().path("id");

        assertThat("Status code is incorrect", statusCode, equalTo(200));
        assertThat("Courier ID is incorrect", courierId, is(not(0)));
    }
}
