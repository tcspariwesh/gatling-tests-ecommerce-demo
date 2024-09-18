package app

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class AppSimulationA extends Simulation {

  val vu: Int = Integer.getInteger("vu", 1)

  val httpProtocol = http
    .baseUrl("https://api-ecomm.gatling.io")
    .acceptHeader("application/json")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:109.0) Gecko/20100101 Firefox/119.0")

  private val scenario1 = scenario("Scenario 1")
    .exec(http("Session").get("/session"))

  private val assertion = global.failedRequests.count.lt(1)

  setUp(
    scenario1.inject(atOnceUsers(vu))
  ).assertions(assertion).protocols(httpProtocol)
}
