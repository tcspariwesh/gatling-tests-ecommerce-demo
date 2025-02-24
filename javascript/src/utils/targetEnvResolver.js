// Resolve environment-specific configuration based on the target environment
const targetEnvResolver = (targetEnv) => {
  switch (targetEnv) {
    case "DEV":
      return {
        pageUrl: "https://ecomm.gatling.io",
        baseUrl: "https://api-ecomm.gatling.io",
        usersFeederFile: "data/users_dev.json",
        productsFeederFile: "data/products_dev.csv"
      };
    default:
      return {
        pageUrl: "https://ecomm.gatling.io",
        baseUrl: "https://api-ecomm.gatling.io",
        usersFeederFile: "data/users_dev.json",
        productsFeederFile: "data/products_dev.csv"
      };
  }
};

export default targetEnvResolver;
