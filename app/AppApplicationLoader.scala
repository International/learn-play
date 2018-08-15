import controllers.Application
import play.api.ApplicationLoader.Context
import play.api._
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc._
import play.api.routing.Router
import router.Routes
import com.softwaremill.macwire._
import _root_.controllers.AssetsComponents
import play.filters.HttpFiltersComponents
import services.{SunService,WeatherService}

class AppApplicationLoader extends ApplicationLoader {
  def load(context: Context): play.api.Application = {
    //    println("loading app")
    LoggerConfigurator(context.environment.classLoader).foreach { cfg => cfg.configure(context.environment) }
    new AppComponents(context).application
  }
}

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context)
  with AhcWSComponents
  with AssetsComponents with HttpFiltersComponents {

  override lazy val controllerComponents = wire[DefaultControllerComponents]
  lazy val prefix: String = "/"
  lazy val router: Router = wire[Routes]
  lazy val applicationController = wire[Application]

}