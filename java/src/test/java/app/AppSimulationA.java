package app;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class AppSimulationA extends Simulation {

  private static final int vu = Integer.getInteger("vu", 1);

  private static final HttpProtocolBuilder httpProtocol =
      http.baseUrl("https://api-ecomm.gatling.io")
          .acceptHeader("application/json")
          .userAgentHeader(
              "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/119.0");

  private static final ScenarioBuilder scenario1 =
      scenario("Scenario 1").exec(http("Session").get("/session"));

  private static final Assertion assertion = global().failedRequests().count().lt(1L);

  {
    setUp(scenario1.injectOpen(atOnceUsers(vu))).assertions(assertion).protocols(httpProtocol);
  }
}
