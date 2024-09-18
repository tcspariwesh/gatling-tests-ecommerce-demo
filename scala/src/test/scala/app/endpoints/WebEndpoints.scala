package app.endpoints

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import app.config.Utils._

object WebEndpoints {

  val homePage = http("HomePage")
    .get(pageUrl)
    .check(status.in(200, 304))

  val loginPage = http("LoginPage")
    .get(s"$pageUrl/login")
    .check(status.in(200, 304))
}
