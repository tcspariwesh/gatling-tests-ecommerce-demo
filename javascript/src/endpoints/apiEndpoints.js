import { ElFileBody, jmesPath } from "@gatling.io/core";
import { http, status } from "@gatling.io/http";

export function withAuthenticationHeader(protocolBuilder) {
  return protocolBuilder.header("Authorization", (session) =>
    session.contains("accessToken") ? session.get("accessToken") : ""
  );
}

export const session = http("Session")
  .get("/session")
  .check(status().is(200))
  .check(jmesPath("sessionId").saveAs("sessionId"));

export const products = http("Product page: #{pageIndex}")
  .get("/products")
  .queryParam("page", "#{pageIndex}")
  .check(status().is(200))
  .check(jmesPath("products").saveAs("products"));

export const search = http("Search")
  .get("/products")
  .queryParam("search", "t-shirt")
  .check(status().is(200));

export const login = http("Login")
  .post("/login")
  .asFormUrlEncoded()
  .formParam("username", "#{username}")
  .formParam("password", "#{password}")
  .check(status().is(200))
  .check(jmesPath("accessToken").saveAs("accessToken"));

export const cart = http("Add to Cart")
  .post("/cart")
  .asJson()
  .body(ElFileBody("bodies/cart.json"))
  .check(status().is(200));

export const checkOut = http("Checkout")
  .post("/checkout")
  .asJson()
  .body(ElFileBody("bodies/cart.json"))
  .check(status().is(200));