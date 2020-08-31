package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import concurrent.duration.{DurationInt}

class ShortUrlGeneratorSimulation extends Simulation {

  val scn = scenario("Anonymous shortening flow")
    .pause(10 seconds)
    .exec(
      http("First request - discard as it times out")
        .post("http://localhost:4000/shorten")
        .header("Content-Type", "application/json")
        .body(StringBody("""{"longUrl": "https://www.google.com"}"""))
    )

    .exec(
      http("Shorten request")
        .post("http://localhost:4000/shorten")
        .header("Content-Type", "application/json")
        .body(StringBody("""{"longUrl": "https://www.google.com"}"""))
        .check(jmesPath("id").saveAs("id"))
    )

    .repeat(10, "n") {
      exec(

        http("Visit shortened url")
          .get({ session => s"""http://localhost:4000/${session("id").as[String]}""" })
          .disableFollowRedirect
          .check(status.is(301))
          .check(header("Location").saveAs("expandedUrl"))
      )
        .exec { session =>
          println(">>>>>> " + session("expandedUrl").as[String] + " <<<<<<<<")
          session
        }
        .pause(1 seconds)
    }


  setUp(
    scn.inject(rampUsers(10) during (5 seconds))
  )
}
