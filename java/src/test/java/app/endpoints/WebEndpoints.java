package app.endpoints;

import static app.config.Utils.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

public class WebEndpoints {

  public static final HttpRequestActionBuilder homePage =
      http("HomePage").get(pageUrl).check(status().in(200, 304));

  public static final HttpRequestActionBuilder loginPage =
      http("LoginPage").get(pageUrl + "/login").check(status().in(200, 304));
  ;
}
