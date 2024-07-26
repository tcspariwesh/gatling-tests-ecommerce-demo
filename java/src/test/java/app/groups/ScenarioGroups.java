package app.groups;

import static app.config.Utils.*;
import static app.endpoints.APIendpoints.*;
import static app.endpoints.WebEndpoints.*;
import static io.gatling.javaapi.core.CoreDsl.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.*;
import java.util.ArrayList;
import java.util.List;

public class ScenarioGroups {

  private record Product(
      int id,
      String name,
      String color,
      String price,
      int quantity,
      String imageSrc,
      String imageAlt) {}

  private static final ObjectMapper mapper = new ObjectMapper();

  private static final FeederBuilder<Object> usersFeeder =
      jsonFile("data/" + usersFeederFile).circular();

  public static final ChainBuilder homeAnonymous =
      group("homeAnonymous")
          .on(homePage, session, exec(session -> session.set("pageIndex", 0)), products);

  public static final ChainBuilder authenticate =
      group("authenticate").on(loginPage, feed(usersFeeder), pause(minSec, maxSec), login);

  public static final ChainBuilder homeAuthenticated =
      group("homeAuthenticated").on(homePage, products, pause(minSec, maxSec), search);

  public static final ChainBuilder addToCart =
      group("addToCart")
          .on(
              exec(
                  session -> {
                    try {
                      List<Product> products =
                          mapper.readValue(
                              session.getString("products"), new TypeReference<List<Product>>() {});
                      Product myFirstProduct = products.get(0);

                      List<Product> cartItems = new ArrayList<>();
                      cartItems.add(myFirstProduct);

                      String cartItemsJsonString = mapper.writeValueAsString(cartItems);
                      return session.set("cartItems", cartItemsJsonString);
                    } catch (Exception e) {
                      throw new RuntimeException(e);
                    }
                  }),
              cart);

  public static final ChainBuilder buy = group("buy").on(checkOut);
}
