package app.groups

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.ChainBuilder
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.util.ArrayList
import java.util.List
import app.config.Utils._
import app.endpoints.APIendpoints._
import app.endpoints.WebEndpoints._

object ScenarioGroups {

  case class Product(id: Int, name: String, color: String, price: Double, quantity: Int, image: String, description: String, imageSrc: String, imageAlt: String)

  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)

  private val usersFeeder = jsonFile("data/" + usersFeederFile).circular

  val homeAnonymous: ChainBuilder = 
    group("homeAnonymous") (
      exec(homePage)
      exec(session)
      exec(session => session.set("pageIndex", 0))
      exec(products)
    )

  val authenticate: ChainBuilder = 
    group("authenticate") (
      exec(loginPage)
      feed(usersFeeder)
      pause(1, 2)
      exec(login)
    )

  val homeAuthenticated: ChainBuilder = 
    group("homeAuthenticated") (
      exec(homePage)
      exec(products)
      pause(1, 2)
      exec(search)
    )

  val addToCart: ChainBuilder = 
  group("addToCart") (
    exec { session =>
      try {
        val products: List[Product] = mapper.readValue(session("products").as[String], new TypeReference[List[Product]](){})
        val myFirstProduct: Product = products.get(0)

        val cartItems: List[Product] = new ArrayList[Product]
        cartItems.add(myFirstProduct)

        val cartItemsJsonString = mapper.writeValueAsString(cartItems)
        session.set("cartItems", cartItemsJsonString)
      } catch {
        case e: Exception => throw new RuntimeException(e)
      }
    }
    exec(cart)
  )

  val buy: ChainBuilder = 
    group("buy") (
      exec(checkOut)
    )
}
