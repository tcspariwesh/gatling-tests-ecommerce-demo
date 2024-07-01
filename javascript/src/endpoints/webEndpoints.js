import { http, status } from "@gatling.io/http";
import { pageUrl } from "../config/utils";

export const homePage = http("HomePage").get(pageUrl).check(status().in(200, 304));

export const loginPage = http("LoginPage")
  .get(`${pageUrl}/login`)
  .check(status().in(200, 304));