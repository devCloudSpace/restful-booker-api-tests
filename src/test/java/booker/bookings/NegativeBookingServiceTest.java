package booker.bookings;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

import booker.BaseBookerTest;
import booker.model.BookingInfo;
import booker.model.BookingInfo.CheckInOutDate;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

public class NegativeBookingServiceTest extends BaseBookerTest {

  private static final String BASE_PATH = "/booking";

  @Test
  public void cannotPatchWithoutToken() {
    String bookingIDpath =
        "/" + given().header("content-type", ContentType.JSON.toString())
            .get(BASE_PATH).then().extract().response().jsonPath().getString("bookingid[0]");

    BookingInfo bookingInfo = BookingInfo.builder()
        .firstname("Jane")
        .build();

    RequestSpecification requestSpec = new RequestSpecBuilder()
        .addHeader("content-type", ContentType.JSON.toString())
        .setConfig(
            config().encoderConfig(encoderConfig().encodeContentTypeAs("Accept: application/json", ContentType.TEXT)))
        .setBody(bookingInfo)
        .build();

    RestAssured.given(requestSpec)
        .when()
        .patch(BASE_PATH + bookingIDpath)
        .then()
        .statusCode(403);
  }

  @Test
  public void cannotUpdateWithoutToken() {
    String bookingIDpath =
        "/" + given().header("content-type", ContentType.JSON.toString())
            .get(BASE_PATH).then().extract().response().jsonPath().getString("bookingid[0]");

    BookingInfo bookingInfo = BookingInfo.builder()
        .firstname("Jane")
        .lastname("Doe")
        .totalprice(42)
        .depositpaid(true)
        .bookingdates(CheckInOutDate.builder()
            .checkin("2020-11-11")
            .checkout("2020-11-13")
            .build())
        .additionalneeds("WakeupCall")
        .build();

    RequestSpecification requestSpec = new RequestSpecBuilder()
        .addHeader("content-type", ContentType.JSON.toString())
        .setConfig(
            config().encoderConfig(encoderConfig().encodeContentTypeAs("Accept: application/json", ContentType.JSON)))
        .setBody(bookingInfo)
        .build();

    RestAssured.given(requestSpec)
        .when()
        .put(BASE_PATH + bookingIDpath)
        .then()
        .statusCode(403);
  }

}