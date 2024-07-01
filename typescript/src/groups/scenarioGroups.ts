import { group, jsonFile, feed, exec } from "@gatling.io/core";
import {
  session,
  login,
  search,
  cart,
  products,
  checkOut
} from "../endpoints/apiEndpoints";
import { homePage, loginPage } from "../endpoints/webEndpoints";

export const usersFeeder = jsonFile("data/users_dev1.json").circular();

export const homeAnonymous = group("homeAnonymous").on(
  homePage,
  session,
  exec((session) => session.set("pageIndex", 0)),
  products
);

export const authenticate = group("authenticate").on(loginPage, feed(usersFeeder), login);

export const homeAuthenticated = group("homeAuthenticated").on(homeAnonymous, products, search);

export const addToCart = group("addToCart").on(
  exec((session) => {
    try {
      const products = JSON.parse(session.get("products"));
      const myFirstProduct = products[0];

      const cartItems = [myFirstProduct];
      const cartItemsJsonString = JSON.stringify(cartItems);

      return session.set("cartItems", cartItemsJsonString);
    } catch (error) {
      console.error("An error occurred:", error);
      return session.set("cartItems", JSON.stringify([]));
    }
  }),
  cart
);

export const buy = group("homeAuthenticated").on(checkOut);