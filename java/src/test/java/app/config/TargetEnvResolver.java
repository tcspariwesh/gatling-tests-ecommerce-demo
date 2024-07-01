package app.config;

public class TargetEnvResolver {

  public record EnvInfo(String pageUrl, String baseUrl, String usersFeederFile) {}

  public static EnvInfo resolveEnvironmentInfo(String targetEnv) {
    return switch (targetEnv) {
      case "DEV1" ->
          new EnvInfo(
              "https://ecomm.sandbox.gatling.io",
              "https://api-ecomm.sandbox.gatling.io",
              "users_dev1.json");
      default ->
          new EnvInfo(
              "https://ecomm.sandbox.gatling.io",
              "https://api-ecomm.sandbox.gatling.io",
              "users_dev1.json");
    };
  }
}
