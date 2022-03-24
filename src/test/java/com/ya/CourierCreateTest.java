package com.ya;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierCreateTest {
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
        ValidatableResponse response = courierClient.create(courier);
        ValidatableResponse responseLogin = courierClient.login(CourierCredentials.from(courier));
        int statusCode = response.extract().statusCode();
        boolean isCourierCreated = response.extract().path("ok");
        int courierId = responseLogin.extract().path("id");

        assertTrue("Courier is not created", isCourierCreated);
        assertThat("Status code is incorrect", statusCode, equalTo(201));
        assertThat("Courier ID is incorrect", courierId, is(not(0)));
    }
    @Test
    public void duplicateCourierCannotBeCreated() {
        courierClient.create(courier);
        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");
        String expected = "Этот логин уже используется";

        assertThat("Status code is incorrect", statusCode, equalTo(409));
        assertEquals("Message is incorrect", expected, errorMessage);
    }

}

