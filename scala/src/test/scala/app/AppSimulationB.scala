package app

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.{ScenarioBuilder, PopulationBuilder}
import app.config.Utils._
import app.groups.ScenarioGroups._
import app.endpoints.APIendpoints.withAuthenticationHeader;


class AppSimulationB extends Simulation {

  private val httpProtocolWithAuthentication =  withAuthenticationHeader(
          http.baseUrl(baseUrl)
              .acceptHeader("application/json")
              .userAgentHeader(
                  "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/119.0"));

  private val scn1: ScenarioBuilder = scenario("Scenario B1")
    .exitBlockOnFail(
      randomSwitch(
        frPerc -> group("fr")(
            exec(homeAnonymous)
            pause(minPauseSec, maxPauseSec)
            exec(authenticate)
            exec(homeAuthenticated)
            pause(minPauseSec, maxPauseSec)
            exec(addToCart)
            pause(minPauseSec, maxPauseSec)
            exec(buy)
        ),
        usPerc -> group("us")(
            exec(homeAnonymous)
            pause(minPauseSec, maxPauseSec)
            exec(authenticate)
            exec(homeAuthenticated)
            pause(minPauseSec, maxPauseSec)
            exec(addToCart)
            pause(minPauseSec, maxPauseSec)
            exec(buy)
        )
      )
    )
    .exitHereIfFailed

  private val scn2: ScenarioBuilder = scenario("Scenario B2")
    .exitBlockOnFail(
      uniformRandomSwitch(
        group("fr")(
            exec(homeAnonymous)
            pause(minPauseSec, maxPauseSec)
            exec(authenticate)
            exec(homeAuthenticated)
            pause(minPauseSec, maxPauseSec)
            exec(addToCart)
            pause(minPauseSec, maxPauseSec)
            exec(buy)
           ),
      group("us")(
            exec(homeAnonymous)
            pause(minPauseSec, maxPauseSec)
            exec(authenticate)
            exec(homeAuthenticated)
            pause(minPauseSec, maxPauseSec)
            exec(addToCart)
            pause(minPauseSec, maxPauseSec)
            exec(buy)
        )
      )
    )
    .exitHereIfFailed

  private def injectionProfile(scn: ScenarioBuilder, profileType: String): PopulationBuilder = {
    profileType match {
      case "capacity" =>
        scn.inject(
          incrementUsersPerSec(users)
            .times(4)
            .eachLevelLasting(duration)
            .separatedByRampsLasting(4)
            .startingFrom(10)
        )
      case "soak" =>
        scn.inject(constantUsersPerSec(users).during(duration))
      case "stress" =>
        scn.inject(stressPeakUsers(users).during(duration))
      case "breakpoint" =>
        scn.inject(rampUsers(users).during(duration))
      case "ramp-hold" =>
        scn.inject(
          rampUsersPerSec(0).to(users).during(rampDuration),
          constantUsersPerSec(users).during(duration)
        )
      case "smoke" =>
        scn.inject(atOnceUsers(1))
      case _ =>
        scn.inject(atOnceUsers(users))
    }
  }

  private def getAssertion(profileType: String): Seq[Assertion] = {
    profileType match {
      case "capacity" | "soak" | "stress" | "breakpoint" | "ramp-hold" =>
        Seq(
          global.responseTime.percentile(90.0).lt(500),
          global.failedRequests.percent.lt(5.0)
        )
      case "smoke" =>
        Seq(global.failedRequests.count.lt(1L))
      case _ =>
        Seq(
          global.responseTime.percentile(90.0).lt(500),
          global.failedRequests.percent.lt(5.0)
        )
    }
  }

  setUp(
    injectionProfile(scn1, testType),
    injectionProfile(scn2, testType)
  ).assertions(
    getAssertion(testType): _*
  ).protocols(httpProtocolWithAuthentication)
}
