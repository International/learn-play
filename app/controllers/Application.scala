package controllers

import java.util.concurrent.TimeUnit

import actors.StatsActor
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import controllers.Assets.Asset
import play.api.mvc._
import services.{SunService, WeatherService}

import scala.concurrent.ExecutionContext.Implicits.global

case class SunInfo(sunrise: String, sunset: String)

class Application (
                    components: ControllerComponents, assets: Assets,
                    sunService: SunService, weatherService: WeatherService,
                    actorSystem: ActorSystem
                  )
    extends AbstractController(components) {
  def index = Action.async {

    val lat = -33.883
    val lon = 151.2167
    implicit val timeout = Timeout(5, TimeUnit.SECONDS)
    val requestsMade = (actorSystem.actorSelection(StatsActor.path) ? StatsActor.GetStats).mapTo[Int]

    for {
      sunInfo <- sunService.getSunInfo(lat, lon)
      requests <- requestsMade
//      temperature <- weatherService.getTemperature(lat, lon)
    } yield {
      Ok(views.html.index(sunInfo, -1, requests))
    }

  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
}
