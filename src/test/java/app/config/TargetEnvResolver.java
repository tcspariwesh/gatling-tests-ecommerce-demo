package app.config;

public class TargetEnvResolver {

  public record EnvInfo(String apiUrl, String usersFeederFile) {}

  public static EnvInfo resolveEnvironmentInfo(String targetEnv) {
    return switch (targetEnv) {
      case "DEV1" ->
          new EnvInfo(
              "https://3wa6f6vn0g.execute-api.eu-west-3.amazonaws.com/app-demo-api-stage1",
              "users_dev1.json");
      default ->
          new EnvInfo(
              "https://3wa6f6vn0g.execute-api.eu-west-3.amazonaws.com/app-demo-api-stage1",
              "users_dev1.json");
    };
  }
}
