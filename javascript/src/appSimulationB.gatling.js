import {
  simulation,
  scenario,
  csv,
  randomSwitch,
  uniformRandomSwitch,
  percent,
  feed,
  group,
  atOnceUsers,
  incrementUsersPerSec,
  constantUsersPerSec,
  stressPeakUsers,
  rampUsersPerSec,
  global
} from "@gatling.io/core";
import { http } from "@gatling.io/http";
import {
  baseUrl,
  duration,
  frDelay,
  frWeight,
  type,
  usDelay,
  usWeight,
  users
} from "./config/utils";
import { withAuthenticationHeader } from "./endpoints/apiEndpoints";
import {
  addToCart,
  authenticate,
  buy,
  homeAnonymous,
  homeAuthenticated
} from "./groups/scenarioGroups";

export default simulation((setUp) => {
  const httpProtocol = withAuthenticationHeader(
    http.baseUrl(baseUrl).acceptHeader("application/json").contentTypeHeader("application/json")
  );

  const frFeeder = csv(
    "performance-data/" + type + "-data/" + type + "_index_" + frDelay + ".csv"
  ).queue();
  const usFeeder = csv(
    "performance-data/" + type + "-data/" + type + "_index_" + usDelay + ".csv"
  ).queue();

  const scenario1 = scenario("Scenario A1")
    .exitBlockOnFail()
    .on(
      randomSwitch().on(
        percent(frWeight).then(
          group("fr").on(
            feed(frFeeder),
            homeAnonymous,
            authenticate,
            homeAuthenticated,
            addToCart,
            buy
          )
        ),
        percent(usWeight).then(
          group("us").on(
            feed(usFeeder),
            homeAnonymous,
            authenticate,
            homeAuthenticated,
            addToCart,
            buy
          )
        )
      )
    )
    .exitHereIfFailed();

  const scenario2 = scenario("Scenario A2")
    .exitBlockOnFail()
    .on(
      uniformRandomSwitch().on(
        group("fr").on(
          feed(frFeeder),
          homeAnonymous,
          authenticate,
          homeAuthenticated,
          addToCart,
          buy
        ),
        group("us").on(
          feed(usFeeder),
          homeAnonymous,
          authenticate,
          homeAuthenticated,
          addToCart,
          buy
        )
      )
    )
    .exitHereIfFailed();

  const getTypeOfLoadTestSc1 = (type) => {
    switch (type) {
      case "capacity":
        return scenario1.injectOpen(
          incrementUsersPerSec(users)
            .times(4)
            .eachLevelLasting(duration)
            .separatedByRampsLasting(4)
            .startingFrom(10)
        );
      case "soak":
        return scenario1.injectOpen(constantUsersPerSec(users).during(duration));
      case "stress":
        return scenario1.injectOpen(stressPeakUsers(users).during(duration));
      case "breakpoint":
        return scenario1.injectOpen(rampUsersPerSec(0).to(users).during(duration));
      case "smoke":
        return scenario1.injectOpen(atOnceUsers(1));
      default:
        return scenario1.injectOpen(atOnceUsers(users));
    }
  };

  const getTypeOfLoadTestSc2 = (type) => {
    switch (type) {
      case "capacity":
        return scenario2.injectOpen(
          incrementUsersPerSec(users)
            .times(4)
            .eachLevelLasting(duration)
            .separatedByRampsLasting(4)
            .startingFrom(10)
        );
      case "soak":
        return scenario2.injectOpen(constantUsersPerSec(users).during(duration));
      case "stress":
        return scenario2.injectOpen(stressPeakUsers(users).during(duration));
      case "breakpoint":
        return scenario2.injectOpen(rampUsersPerSec(0).to(users).during(duration));
      case "smoke":
        return scenario2.injectOpen(atOnceUsers(1));
      default:
        return scenario2.injectOpen(atOnceUsers(users));
    }
  };

  const getAssertion = (type) => {
    switch (type) {
      case "capacity":
        return global().responseTime().percentile(90.0).lt(500);
      case "soak":
        return global().responseTime().percentile(99.9).lt(500);
      case "stress":
        return global().responseTime().percentile(90.0).lt(500);
      case "breakpoint":
        return global().responseTime().percentile(90.0).lt(500);
      case "smoke":
        return global().failedRequests().count().lt(1.0);
      default:
        return global().responseTime().percentile(90.0).lt(500);
    }
  };

  setUp(getTypeOfLoadTestSc1(type), getTypeOfLoadTestSc2(type))
    .assertions(getAssertion(type))
    .protocols(httpProtocol);
});