package app.config;

public class Utils {

  public static final String TARGET_ENV = System.getProperty("TARGET_ENV", "PROD");

  public static final int users = Integer.getInteger("USERS", 1);
  public static final int duration = Integer.getInteger("DURATION", 1);
  public static final int minSec = Integer.getInteger("MIN", 5);
  public static final int maxSec = Integer.getInteger("MAX", 15);
  public static final String type = System.getProperty("TYPE", "smoke");

  public static final int frPerc = Integer.getInteger("FR_PERC", 60);
  public static final int usPerc = Integer.getInteger("US_PERC", 40);

  public static final String frDelay = System.getProperty("FR_PERF_INDEX", "0");
  public static final String usDelay = System.getProperty("US_PERF_INDEX", "0");

  public static final TargetEnvResolver.EnvInfo envInfo =
      TargetEnvResolver.resolveEnvironmentInfo(TARGET_ENV);
  public static final String pageUrl = envInfo.pageUrl();
  public static final String baseUrl = envInfo.baseUrl();
  public static final String usersFeederFile = envInfo.usersFeederFile();
}
