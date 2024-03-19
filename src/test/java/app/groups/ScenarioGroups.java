package app.groups;

import static app.config.Utils.usersFeederFile;
import static io.gatling.javaapi.core.CoreDsl.*;

import app.endpoints.APIendpoints;
import io.gatling.javaapi.core.*;

public class ScenarioGroups {

  private static final FeederBuilder<Object> usersFeeder =
      jsonFile("data/" + usersFeederFile).circular();

  public static final ChainBuilder authenticate =
      group("authenticate").on(APIendpoints.session, feed(usersFeeder), APIendpoints.authenticate);

  public static final ChainBuilder browse =
      group("browse").on(APIendpoints.list, APIendpoints.search);

  public static final ChainBuilder complete_checkout =
      group("complete_checkout")
          .on(APIendpoints.addToCart, APIendpoints.fetchCartItems, APIendpoints.order);

  public static final ChainBuilder incomplete_checkout =
      group("incomplete_checkout").on(APIendpoints.addToCart, APIendpoints.fetchCartItems);
}
