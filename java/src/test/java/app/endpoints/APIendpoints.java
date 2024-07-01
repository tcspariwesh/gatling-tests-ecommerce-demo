package app.endpoints;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.http.*;

public class APIendpoints {

  public static final HttpProtocolBuilder withAuthenticationHeader(
      HttpProtocolBuilder protocolBuilder) {
    return protocolBuilder.header(
        "Authorization",
        session -> session.contains("accessToken") ? session.getString("accessToken") : "");
  }

  public static final HttpRequestActionBuilder session =
      http("Session")
          .get("/session")
          .queryParam("delay", "0")
          .check(jmesPath("sessionId").saveAs("sessionId"));

  public static final HttpRequestActionBuilder products =
      http("Product page: #{pageIndex}")
          .get("/products")
          .queryParam("page", "#{pageIndex}")
          .queryParam("delay", "0")
          .check(status().is(200))
          .check(jmesPath("products").saveAs("products"));

  public static final HttpRequestActionBuilder login =
      http("Login")
          .post("/login")
          .asFormUrlEncoded()
          .formParam("username", "#{username}")
          .formParam("password", "#{password}")
          .check(status().is(200))
          .check(jmesPath("accessToken").saveAs("accessToken"));

  public static final HttpRequestActionBuilder search =
      http("Search")
          .get("/products")
          .queryParam("search", "t-shirt")
          .queryParam("delay", "#{delay}");

  public static final HttpRequestActionBuilder cart =
      http("Update Cart")
          .put("/cart")
          .asJson()
          .body(ElFileBody("bodies/cart.json"))
          .check(status().is(200));

  public static final HttpRequestActionBuilder checkOut =
      http("Checkout")
          .post("/checkout")
          .asJson()
          .body(ElFileBody("bodies/cart.json"))
          .check(status().in(200));
}
