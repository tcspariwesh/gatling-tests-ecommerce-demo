package app.config;

public class Utils {

  public static final String TARGET_ENV = System.getProperty("TARGET_ENV", "dev1");

  public static final int users = Integer.getInteger("USERS", 1);
  public static final int duration = Integer.getInteger("DURATION", 1);

  public static final String type = System.getProperty("TYPE", "smoke");

  public static final int frWeight = Integer.getInteger("FR_WEIGHT", 60);
  public static final int usWeight = Integer.getInteger("US_WEIGHT", 0);

  public static final String frDelay = System.getProperty("FR_PERF_INDEX", "0");
  public static final String usDelay = System.getProperty("US_PERF_INDEX", "0");

  public static final TargetEnvResolver.EnvInfo envInfo =
      TargetEnvResolver.resolveEnvironmentInfo(TARGET_ENV);
  public static final String pageUrl = envInfo.pageUrl();
  public static final String baseUrl = envInfo.baseUrl();
  public static final String usersFeederFile = envInfo.usersFeederFile();
}