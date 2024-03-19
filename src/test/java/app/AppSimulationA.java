package app;

import static app.config.Utils.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import app.endpoints.APIendpoints;
import app.groups.ScenarioGroups;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class AppSimulationA extends Simulation {

  private static final HttpProtocolBuilder httpProtocolWithAuthentication =
      APIendpoints.withAuthenticationHeader(
          http.baseUrl(baseUrl)
              .acceptHeader(
                  "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
              .inferHtmlResources()
              .acceptEncodingHeader("gzip, deflate")
              .acceptLanguageHeader("fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7")
              .userAgentHeader(
                  "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
              .silentResources());

  private static final FeederBuilder<String> frFeeder =
      csv("performance-data/" + type + "-data/" + type + "_index_" + frDelay + ".csv").queue();
  private static final FeederBuilder<String> usFeeder =
      csv("performance-data/" + type + "-data/" + type + "_index_" + usDelay + ".csv").queue();

  private static final ScenarioBuilder scenario1 =
      scenario("Scenario A1")
          .exitBlockOnFail()
          .on(
              randomSwitch()
                  .on(
                      percent(frWeight)
                          .then(
                              group("fr")
                                  .on(
                                      feed(frFeeder),
                                      ScenarioGroups.authenticate,
                                      ScenarioGroups.browse,
                                      ScenarioGroups.complete_checkout)),
                      percent(usWeight)
                          .then(
                              group("us")
                                  .on(
                                      feed(usFeeder),
                                      ScenarioGroups.authenticate,
                                      ScenarioGroups.browse,
                                      ScenarioGroups.complete_checkout))))
          .exitHereIfFailed();

  private static final ScenarioBuilder scenario2 =
      scenario("Scenario A2")
          .exitBlockOnFail()
          .on(
              group("fr")
                  .on(
                      feed(frFeeder),
                      ScenarioGroups.authenticate,
                      ScenarioGroups.browse,
                      ScenarioGroups.incomplete_checkout),
              group("us")
                  .on(
                      feed(usFeeder),
                      ScenarioGroups.authenticate,
                      ScenarioGroups.browse,
                      ScenarioGroups.incomplete_checkout))
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
      case "breakpoint" -> scenario1.injectOpen(rampUsersPerSec(0).to(users).during(duration));
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
      case "breakpoint" -> scenario2.injectOpen(rampUsersPerSec(0).to(users).during(duration));
      case "smoke" -> scenario2.injectOpen(atOnceUsers(1));
      default -> scenario2.injectOpen(atOnceUsers(users));
    };
  }

  private static final Assertion getAssertion(String type) {
    return switch (type) {
      case "capacity" -> global().responseTime().percentile(90.0).lt(500);
      case "soak" -> global().responseTime().percentile(99.9).lt(500);
      case "stress" -> global().responseTime().percentile(90.0).lt(500);
      case "breakpoint" -> global().responseTime().percentile(90.0).lt(500);
      case "smoke" -> global().failedRequests().count().lt(1L);
      default -> global().responseTime().percentile(90.0).lt(500);
    };
  }

  {
    setUp(getTypeOfLoadTestSc1(type), getTypeOfLoadTestSc2(type))
        .assertions(getAssertion(Assertiontype))
        .protocols(httpProtocolWithAuthentication);
  }
}
