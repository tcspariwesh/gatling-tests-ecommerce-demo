const targetEnvResolver = (targetEnv) => {
    switch (targetEnv) {
        case 'DEV1':
            return {pageUrl: "https://ecomm.sandbox.gatling.io", baseUrl: "https://api-ecomm.sandbox.gatling.io", usersFeederFile: "users_dev1.json"}
        default:
            return {pageUrl: "https://ecomm.sandbox.gatling.io", baseUrl: "https://api-ecomm.sandbox.gatling.io", usersFeederFile: "users_dev1.json"}
      }
}

export default targetEnvResolver;