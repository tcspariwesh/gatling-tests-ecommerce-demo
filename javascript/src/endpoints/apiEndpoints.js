import { ElFileBody, jmesPath } from "@gatling.io/core";
import { http, status } from "@gatling.io/http";

export function withAuthenticationHeader(protocolBuilder) {
  return protocolBuilder.header("Authorization", (session) =>
    session.contains("accessToken") ? session.get("accessToken") : ""
  );
}

export const session = http("Session")
  .get("/session")
  .queryParam("delay", "0")
  .check(status().is(200))
  .check(jmesPath("sessionId").saveAs("sessionId"));

export const products = http("Product page: #{pageIndex}")
  .get("/products")
  .queryParam("page", "#{pageIndex}")
  .queryParam("delay", "0")
  .check(status().is(200))
  .check(jmesPath("products").saveAs("products"));

export const search = http("Search")
  .get("/products")
  .queryParam("search", "t-shirt")
  .queryParam("delay", "#{delay}");

export const login = http("Login")
  .post("/login")
  .formParam("username", "#{username}")
  .formParam("password", "#{password}")
  .check(status().is(200))
  .check(jmesPath("accessToken").saveAs("accessToken"));

export const cart = http("Update Cart")
  .put("/cart")
  .asJson()
  .body(ElFileBody("bodies/cart.json"))
  .check(status().is(200));

export const checkOut = http("Checkout")
  .post("/checkout")
  .asJson()
  .body(ElFileBody("bodies/cart.json"))
  .check(status().is(200));