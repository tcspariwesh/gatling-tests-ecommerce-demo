package app.config

import scala.concurrent.duration._

object Utils {

  val targetEnv: String = System.getProperty("targetEnv", "PROD")

  val users: Int = Integer.getInteger("users", 1)
  val duration: FiniteDuration = Duration(java.lang.Long.getLong("durationMinutes", 1), MINUTES)
  val rampDuration: FiniteDuration = Duration(java.lang.Long.getLong("rampDurationMinutes", 1), MINUTES)

  var minPauseSec : Integer = Integer.getInteger("minPauseSec", 5 )
  var maxPauseSec : Integer = Integer.getInteger("maxPauseSec", 15 )

  val testType: String = System.getProperty("type", "smoke")

  val frPerc: Double = Option(System.getProperty("frPerc")).map(_.toDouble).getOrElse(60.0)
  val usPerc: Double = Option(System.getProperty("usPerc")).map(_.toDouble).getOrElse(40.0)

  val frDelay: String = System.getProperty("frDelay", "0")
  val usDelay: String = System.getProperty("usDelay", "0")

  val envInfo = TargetEnvResolver.resolveEnvironmentInfo(targetEnv)
  val pageUrl: String = envInfo.pageUrl  
  val baseUrl: String = envInfo.baseUrl  
  val usersFeederFile: String = envInfo.usersFeederFile 
}
