package com.ya;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import java.util.Random;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CourierDeleteTest {
    private Courier courier;
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courier = Courier.getRandom();
        courierClient = new CourierClient();
    }
    @Test
    public void checkCourierCanBeDeleted() {
        courierClient.create(courier);
        ValidatableResponse responseLogin = courierClient.login(CourierCredentials.from(courier));
        int courierId = responseLogin.extract().path("id");
        ValidatableResponse responseDelete = courierClient.delete(courierId);
        int statusCode = responseDelete.extract().statusCode();
        boolean isCourierDeleted = responseDelete.extract().path("ok");

        assertTrue("Courier is not created", isCourierDeleted);
        assertThat("Status code is incorrect", statusCode, equalTo(200));
    }
    @Test
    public void checkCourierValidationDeleteWithNonexistentId(){
        int courierId =new Random().nextInt();
        ValidatableResponse response = courierClient.delete(courierId);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");
        String expectedErrorMessage = "Курьера с таким id нет";

        assertEquals("Status code is incorrect",404, statusCode);
        assertEquals("Message is incorrect", expectedErrorMessage, errorMessage);
    }
    @Test
    public void checkCourierValidationDeleteWithoutId() {
        ValidatableResponse response = courierClient.deleteWithoutId();
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");
        String expectedErrorMessage = "Недостаточно данных для удаления курьера";

        assertEquals("Status code is incorrect",404, statusCode);
        assertEquals("Message is incorrect", expectedErrorMessage, errorMessage);
    }
    }






