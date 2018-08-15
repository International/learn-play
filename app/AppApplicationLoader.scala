import controllers.Application
import play.api.ApplicationLoader.Context
import play.api._
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc._
import play.api.routing.Router
import router.Routes
import com.softwaremill.macwire._
import _root_.controllers.AssetsComponents
import actors.StatsActor
import actors.StatsActor.Ping
import akka.actor.Props
import filters.StatsFilter
import play.filters.HttpFiltersComponents
import services.{SunService, WeatherService}

import scala.concurrent.Future

class AppApplicationLoader extends ApplicationLoader {
  def load(context: Context): play.api.Application = {
    println("loading app")
    LoggerConfigurator(context.environment.classLoader).foreach { cfg => cfg.configure(context.environment) }
    new AppComponents(context).application
  }
}

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with AhcWSComponents
  with AssetsComponents with HttpFiltersComponents {

  override lazy val controllerComponents = wire[DefaultControllerComponents]

  lazy val statsFilter:Filter = wire[StatsFilter]
  override lazy val httpFilters = Seq(statsFilter)

  lazy val prefix: String = "/"
  lazy val router: Router = wire[Routes]

  lazy val sunService = wire[SunService]
  lazy val weatherService = wire[WeatherService]

  lazy val applicationController = wire[Application]

  lazy val statsActor = actorSystem.actorOf(Props(wire[StatsActor]), StatsActor.name)

  applicationLifecycle.addStopHook { () =>
    Logger.info("app is about to stop")
    Future.successful(Unit)
  }

  val onStart = {
    Logger.info("app is about to start")
    statsActor ! Ping
  }
}