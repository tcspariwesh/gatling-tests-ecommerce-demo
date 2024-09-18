package app.endpoints

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

object APIendpoints {

  def withAuthenticationHeader(protocolBuilder: HttpProtocolBuilder): HttpProtocolBuilder = {
    protocolBuilder.header(
      "Authorization",
      session => if (session.contains("accessToken")) session("accessToken").as[String] else ""
    )
  }

  val session = http("Session")
    .get("/session")
    .check(status.is(200))
    .check(jmesPath("sessionId").saveAs("sessionId"))

  val products = http("Product page: #{pageIndex}")
    .get("/products")
    .queryParam("page", "#{pageIndex}")
    .check(status.is(200))
    .check(jmesPath("products").saveAs("products"))

  val login = http("Login")
    .post("/login")
    .formParam("username", "#{username}")
    .formParam("password", "#{password}")
    .check(status.is(200))
    .check(jmesPath("accessToken").saveAs("accessToken"))

  val search = http("Search")
    .get("/products")
    .queryParam("search", "t-shirt")
    .check(status.is(200))

  val cart = http("Add to Cart")
    .post("/cart")
    .asJson
    .body(ElFileBody("bodies/cart.json"))
    .check(status.is(200))

  val checkOut = http("Checkout")
    .post("/checkout")
    .asJson
    .body(ElFileBody("bodies/cart.json"))
    .check(status.is(200))
}
