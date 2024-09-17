import { simulation, scenario, atOnceUsers, global } from "@gatling.io/core";
import { http } from "@gatling.io/http";

export default simulation((setUp) => {
  const httpProtocol = http
    .baseUrl("https://api-ecomm.gatling.io")
    .acceptHeader("application/json")
    .userAgentHeader(
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/119.0"
    );

  const scenario1 = scenario("Scenario 1").exec(http("Session").get("/session"));

  const assertion = global().failedRequests().count().lt(1.0);

  setUp(scenario1.injectOpen(atOnceUsers(1)))
    .assertions(assertion)
    .protocols(httpProtocol);
});
