const targetEnvResolver = (targetEnv: String) => {
    switch (targetEnv) {
        case 'PROD':
            return {pageUrl: "https://ecomm.gatling.io", baseUrl: "https://api-ecomm.gatling.io", usersFeederFile: "users_dev1.json"}
        default:
            return {pageUrl: "https://ecomm.gatling.io", baseUrl: "https://api-ecomm.gatling.io", usersFeederFile: "users_dev1.json"}
      }
}

export default targetEnvResolver;