package app.config

object TargetEnvResolver {

  case class EnvInfo(pageUrl: String, baseUrl: String, usersFeederFile: String)

  def resolveEnvironmentInfo(targetEnv: String): EnvInfo = targetEnv match {
    case "PROD" =>
      EnvInfo("https://ecomm.gatling.io", "https://api-ecomm.gatling.io", "users_dev1.json")
    case _ =>
      EnvInfo("https://ecomm.gatling.io", "https://api-ecomm.gatling.io", "users_dev1.json")
  }
}
