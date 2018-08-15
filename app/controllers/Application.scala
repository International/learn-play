package controllers

import controllers.Assets.Asset
import javax.inject._
import play.api.libs.ws.WSClient
import play.api.mvc._
import services.{SunService, WeatherService}

import scala.concurrent.ExecutionContext.Implicits.global

case class SunInfo(sunrise: String, sunset: String)

class Application (components: ControllerComponents, assets: Assets, ws: WSClient)
    extends AbstractController(components) {
  def index = Action.async {

    val sunService = new SunService(ws)
    val weatherService = new WeatherService(ws)
    val lat = -33.883
    val lon = 151.2167

    for {
      sunInfo <- sunService.getSunInfo(lat, lon)
      temperature <- weatherService.getTemperature(lat, lon)
    } yield {
      Ok(views.html.index(sunInfo, temperature))
    }

  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
}
