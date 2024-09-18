package app;

import static app.config.Utils.*;
import static app.endpoints.APIendpoints.withAuthenticationHeader;
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

  private static final ScenarioBuilder scn1 =
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
                                      pause(minPauseSec, maxPauseSec),
                                      authenticate,
                                      homeAuthenticated,
                                      pause(minPauseSec, maxPauseSec),
                                      addToCart,
                                      pause(minPauseSec, maxPauseSec),
                                      buy)),
                      percent(usPerc)
                          .then(
                              group("us")
                                  .on(
                                      homeAnonymous,
                                      pause(minPauseSec, maxPauseSec),
                                      authenticate,
                                      homeAuthenticated,
                                      pause(minPauseSec, maxPauseSec),
                                      addToCart,
                                      pause(minPauseSec, maxPauseSec),
                                      buy))))
          .exitHereIfFailed();

  private static final ScenarioBuilder scn2 =
      scenario("Scenario B2")
          .exitBlockOnFail()
          .on(
              uniformRandomSwitch()
                  .on(
                      group("fr")
                          .on(
                              homeAnonymous,
                              pause(minPauseSec, maxPauseSec),
                              authenticate,
                              homeAuthenticated,
                              pause(minPauseSec, maxPauseSec),
                              addToCart,
                              pause(minPauseSec, maxPauseSec),
                              buy),
                      group("us")
                          .on(
                              homeAnonymous,
                              pause(minPauseSec, maxPauseSec),
                              authenticate,
                              homeAuthenticated,
                              pause(minPauseSec, maxPauseSec),
                              addToCart,
                              pause(minPauseSec, maxPauseSec),
                              buy)))
          .exitHereIfFailed();

  private static final PopulationBuilder injectionProfile(ScenarioBuilder scn, String type) {
    return switch (type) {
      case "capacity" ->
          scn.injectOpen(
              incrementUsersPerSec(users)
                  .times(4)
                  .eachLevelLasting(duration)
                  .separatedByRampsLasting(4)
                  .startingFrom(10));
      case "soak" -> scn.injectOpen(constantUsersPerSec(users).during(duration));
      case "stress" -> scn.injectOpen(stressPeakUsers(users).during(duration));
      case "breakpoint" -> scn.injectOpen(rampUsers(users).during(duration));
      case "ramp-hold" ->
          scn.injectOpen(
              rampUsersPerSec(0).to(users).during(ramp_duration),
              constantUsersPerSec(users).during(duration));
      case "smoke" -> scn.injectOpen(atOnceUsers(1));
      default -> scn.injectOpen(atOnceUsers(users));
    };
  }

  private static final List<Assertion> assertions =
      List.of(
          global().responseTime().percentile(90.0).lt(500),
          global().failedRequests().percent().lt(5.0));

  private static final List<Assertion> getAssertion(String type) {
    return switch (type) {
      case "capacity" -> assertions;
      case "soak" -> assertions;
      case "stress" -> assertions;
      case "breakpoint" -> assertions;
      case "ramp-hold" -> assertions;
      case "smoke" -> List.of(global().failedRequests().count().lt(1L));
      default -> assertions;
    };
  }

  {
    setUp(injectionProfile(scn1, testType), injectionProfile(scn2, testType))
        .assertions(getAssertion(testType))
        .protocols(httpProtocolWithAuthentication);
  }
}
