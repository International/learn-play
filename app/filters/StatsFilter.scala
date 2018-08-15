package filters

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.Logger
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.Future
class StatsFilter(actorSystem: ActorSystem, implicit val mat: Materializer) extends Filter {
  override def apply(nextFilter: (RequestHeader) => Future[Result]) (header: RequestHeader): Future[Result] = {
    Logger.info(s"serving another request at ${header.path}")
    nextFilter(header)
  }
}
