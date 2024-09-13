package app;

import static app.config.Utils.*;
import static app.endpoints.APIendpoints.*;
import static app.groups.ScenarioGroups.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import java.util.List;

public class AppSimulationB extends Simulation {

  private static final HttpProtocolBuilder httpProtocolWithAuthentication =
      withAuthenticationHeader(
          http.baseUrl(baseUrl)
              .acceptHeader("application/json")
              .userAgentHeader(
                  "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/119.0"));

  private static final ScenarioBuilder scenario1 =
      scenario("Scenario B1")
          .exitBlockOnFail()
          .on(
              randomSwitch()
                  .on(
                      percent(frPerc)
                          .then(
                              group("fr")
                                  .on(
                                      homeAnonymous,
                                      pause(minSec, maxSec),
                                      authenticate,
                                      homeAuthenticated,
                                      pause(minSec, maxSec),
                                      addToCart,
                                      pause(minSec, maxSec),
                                      buy)),
                      percent(usPerc)
                          .then(
                              group("us")
                                  .on(
                                      homeAnonymous,
                                      pause(minSec, maxSec),
                                      authenticate,
                                      homeAuthenticated,
                                      pause(minSec, maxSec),
                                      addToCart,
                                      pause(minSec, maxSec),
                                      buy))))
          .exitHereIfFailed();

  private static final ScenarioBuilder scenario2 =
      scenario("Scenario B2")
          .exitBlockOnFail()
          .on(
              uniformRandomSwitch()
                  .on(
                      group("fr")
                          .on(
                              homeAnonymous,
                              pause(minSec, maxSec),
                              authenticate,
                              homeAuthenticated,
                              pause(minSec, maxSec),
                              addToCart,
                              pause(minSec, maxSec),
                              buy),
                      group("us")
                          .on(
                              homeAnonymous,
                              pause(minSec, maxSec),
                              authenticate,
                              homeAuthenticated,
                              pause(minSec, maxSec),
                              addToCart,
                              pause(minSec, maxSec),
                              buy)))
          .exitHereIfFailed();

  private static final PopulationBuilder getTypeOfLoadTestSc1(String type) {
    return switch (type) {
      case "capacity" ->
          scenario1.injectOpen(
              incrementUsersPerSec(users)
                  .times(4)
                  .eachLevelLasting(duration)
                  .separatedByRampsLasting(4)
                  .startingFrom(10));
      case "soak" -> scenario1.injectOpen(constantUsersPerSec(users).during(duration));
      case "stress" -> scenario1.injectOpen(stressPeakUsers(users).during(duration));
      case "breakpoint" -> scenario1.injectOpen(rampUsers(users).during(duration));
      case "smoke" -> scenario1.injectOpen(atOnceUsers(1));
      default -> scenario1.injectOpen(atOnceUsers(users));
    };
  }

  private static final PopulationBuilder getTypeOfLoadTestSc2(String type) {
    return switch (type) {
      case "capacity" ->
          scenario2.injectOpen(
              incrementUsersPerSec(users)
                  .times(4)
                  .eachLevelLasting(duration)
                  .separatedByRampsLasting(4)
                  .startingFrom(10));
      case "soak" -> scenario2.injectOpen(constantUsersPerSec(users).during(duration));
      case "stress" -> scenario2.injectOpen(stressPeakUsers(users).during(duration));
      case "breakpoint" -> scenario2.injectOpen(rampUsers(users).during(duration));
      case "smoke" -> scenario2.injectOpen(atOnceUsers(1));
      default -> scenario2.injectOpen(atOnceUsers(users));
    };
  }

  private static final List<Assertion> getAssertion(String type) {
    return switch (type) {
      case "capacity" ->
          List.of(
              global().responseTime().percentile(90.0).lt(500),
              global().failedRequests().percent().lt(5.0));
      case "soak" ->
          List.of(
              global().responseTime().percentile(90.0).lt(500),
              global().failedRequests().percent().lt(5.0));
      case "stress" ->
          List.of(
              global().responseTime().percentile(90.0).lt(500),
              global().failedRequests().percent().lt(5.0));
      case "breakpoint" ->
          List.of(
              global().responseTime().percentile(90.0).lt(500),
              global().failedRequests().percent().lt(5.0));
      case "smoke" -> List.of(global().failedRequests().count().lt(1L));
      default ->
          List.of(
              global().responseTime().percentile(90.0).lt(500),
              global().failedRequests().percent().lt(5.0));
    };
  }

  {
    setUp(getTypeOfLoadTestSc1(type), getTypeOfLoadTestSc2(type))
        .assertions(getAssertion(type))
        .protocols(httpProtocolWithAuthentication);
  }
}
