package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class ParseJsonSimulation extends Simulation {

  val scn = scenario("JSON parsing")
    .exec(
      http("GET")
        .get("http://jsonplaceholder.typicode.com/comments")
    )


  setUp(
    scn.inject(atOnceUsers(1))
  )
}
