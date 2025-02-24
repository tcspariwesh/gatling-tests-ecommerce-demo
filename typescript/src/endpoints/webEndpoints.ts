import { http, status } from "@gatling.io/http";
import { pageUrl } from "../utils/config";

// Define the home page request with response status validation
// Reference: https://docs.gatling.io/reference/script/protocols/http/request/#checks
export const homePage = http("HomePage").get(pageUrl).check(status().in(200, 304)); // Accept both OK (200) and Not Modified (304) statuses

// Define the login page request with response status validation
// Reference: https://docs.gatling.io/reference/script/protocols/http/request/#checks
export const loginPage = http("LoginPage").get(`${pageUrl}/login`).check(status().in(200, 304));
