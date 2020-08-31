package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class ParseJsonSimulation extends Simulation {

  val scn = scenario("JSON parsing")
    .exec(
      http("GET")
        .get("http://jsonplaceholder.typicode.com/comments")
        .check(jmesPath("[0].name").saveAs("commentName"))
    )

    .exec(
      http("PATCH")
        .patch("http://jsonplaceholder.typicode.com/comments/1")
        .header("Content-Type", "application/json")
        .body(StringBody { session =>
          val commentName = session("commentName").as[String]
          s"""{"name": "FOO ${commentName.reverse}"}"""
        })
    )


  setUp(
    scn.inject(atOnceUsers(1))
  )
}
