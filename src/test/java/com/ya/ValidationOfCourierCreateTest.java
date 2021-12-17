package com.ya;

import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ValidationOfCourierCreateTest {


    private CourierClient courierClient;
    private int courierId;
    private int expectedStatus;
    private String expectedErrorMessage;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    public ValidationOfCourierCreateTest(Courier courier,int expectedStatus, String expectedErrorMessage){
        this.courier = courier;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }
    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {Courier.getWithLoginOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getWithPasswordOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getWithFirstNameOnly(), 400, "Недостаточно данных для создания учетной записи"},
                {Courier.getWithoutFirstNameOnly(), 201, null}
        };
    }
    @Test
    public void checkValidationOfCourierCreationTest(){
            ValidatableResponse response = new CourierClient().create(courier);
            int statusCode = response.extract().statusCode();
            String errorMessage = response.extract().path("message");
            if(statusCode == 201){
                courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");
                assertThat("Courier ID is incorrect", courierId, is(not(0)));
            }
            assertEquals("Status code is incorrect", expectedStatus, statusCode);
            assertEquals("Message is incorrect", expectedErrorMessage, errorMessage);
    }
}
