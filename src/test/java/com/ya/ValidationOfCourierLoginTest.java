package com.ya;

import io.restassured.response.ValidatableResponse;
import org.junit.After;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
@RunWith(Parameterized.class)
public class ValidationOfCourierLoginTest {


    private static CourierClient courierClient = new CourierClient();
    private static Courier courier = Courier.getRandom();
    private int courierId;
    private int expectedStatus;
    private String expectedErrorMessage;
    private CourierCredentials courierCredentials;


    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    public ValidationOfCourierLoginTest(CourierCredentials courierCredentials,int expectedStatus, String expectedErrorMessage){
        this.courierCredentials = courierCredentials;
        this.expectedStatus = expectedStatus;
        this.expectedErrorMessage = expectedErrorMessage;
    }
    @Parameterized.Parameters
    public static Object[][] getTestData() {

        return new Object[][]{
                {CourierCredentials.getCourierAuthorizationWithLoginOnly(courier), 400, "Недостаточно данных для входа"},
                {CourierCredentials.getCourierAuthorizationWithPasswordOnly(courier), 400, "Недостаточно данных для входа"},
                {CourierCredentials.getCourierAuthorizationWithNotValidCredentials(), 404, "Учетная запись не найдена"},

        };
    }
    @Test
    public void checkValidationOfCourierCreationTest(){
        courierClient.create(courier);
        ValidatableResponse errorResponse = new CourierClient().login(courierCredentials);
        int statusCode = errorResponse.extract().statusCode();
        assertEquals("Status code is incorrect", expectedStatus, statusCode);
        String errorMessage = errorResponse.extract().path("message");
        assertEquals("Message is incorrect", expectedErrorMessage, errorMessage);
    }
}
