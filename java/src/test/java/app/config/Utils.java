package app.config;

import java.time.Duration;

public class Utils {

  public static final String targetEnv = System.getProperty("targetEnv", "PROD");

  public static final int users = Integer.getInteger("users", 1);
  public static final Duration duration =
      Duration.ofMinutes(Integer.getInteger("durationMinutes", 1));
  public static final Duration ramp_duration =
      Duration.ofMinutes(Integer.getInteger("rampDurationMinutes", 1));
  public static final int minPauseSec = Integer.getInteger("minPauseSec", 5);
  public static final int maxPauseSec = Integer.getInteger("maxPauseSec", 15);
  public static final String testType = System.getProperty("testType", "smoke");

  public static final int frPerc = Integer.getInteger("frPerc", 60);
  public static final int usPerc = Integer.getInteger("usPerc", 40);

  public static final String frDelay = System.getProperty("frDelay", "0");
  public static final String usDelay = System.getProperty("usDelay", "0");

  public static final TargetEnvResolver.EnvInfo envInfo =
      TargetEnvResolver.resolveEnvironmentInfo(targetEnv);
  public static final String pageUrl = envInfo.pageUrl();
  public static final String baseUrl = envInfo.baseUrl();
  public static final String usersFeederFile = envInfo.usersFeederFile();
}
