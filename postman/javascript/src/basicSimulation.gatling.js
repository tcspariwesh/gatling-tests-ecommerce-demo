import { simulation, atOnceUsers, global, scenario, getParameter } from "@gatling.io/core";
import { postman, postmanProtocol } from "@gatling.io/postman";

export default simulation((setUp) => {
  // Load VU count from system properties
  // Reference: https://docs.gatling.io/guides/passing-parameters/
  const vu = parseInt(getParameter("vu", "1"));

  // Define the postman collection with its corresponding environment
  // Reference: https://docs.gatling.io/reference/script/protocols/postman/#import-collections
  const collection = postman
    .fromResource("gatlingEcommerce.postman_collection.json")
    .environment("gatlingEcommerce.postman_environment.json");

  // Define HTTP protocol without any configuration
  // Reference: https://docs.gatling.io/reference/script/protocols/http/protocol/
  // Reference: https://docs.gatling.io/reference/script/protocols/postman/#dsl-overview
  const basePostmanProtocol = postmanProtocol(collection);

  // Define scenario
  // Reference: https://docs.gatling.io/reference/script/core/scenario/
  const scn = scenario("Scenario").exec(
    // Initialize the Postman scoped variables. This is not automated yet, expect when using collection.scenario().
    collection.initVariables,
    // Call the authentication endpoint by referencing the corresponding [folder > subfolders > request] in the postman collection
    collection.folder("API Endpoints").folder("Authentication").request("Create User Session")
  );

  // Define assertions
  // Reference: https://docs.gatling.io/reference/script/core/assertions/
  const assertion = global().failedRequests().count().lt(1.0);

  // Define injection profile and execute the test
  // Reference: https://docs.gatling.io/reference/script/core/injection/
  setUp(scn.injectOpen(atOnceUsers(vu)))
    .assertions(assertion)
    .protocols(basePostmanProtocol);
});
