

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.routing.RoundRobinRouter
import akka.actor.ActorRef
import akka.actor.ActorLogging
import akka.actor.Cancellable

class MasterActor extends Actor with ActorLogging {
  import context._
  private var scheduler: Cancellable = _

  var singleRcptor: ActorRef = _ //context.actorOf(Props[SensorRecepient])
  var router: ActorRef = _ //context.actorOf(Props.empty.withRouter(RoundRobinRouter(routees = Vector[ActorRef](singleRcptor))))

  var generator1: ActorRef = _

  override def preStart(): Unit = {

    singleRcptor = context.actorOf(Props[SensorRecepient])
    router = context.actorOf(Props.empty.withRouter(RoundRobinRouter(routees = Vector[ActorRef](singleRcptor))))
    generator1 = context.actorOf(Props(new SensorGenerator(router, singleRcptor)), name = "generator1")
    
  }

  def receive() = {

    case ShutDown =>
      log.info("INFO | Generatore be requested to graceful shutdown ...")
      context.system.shutdown

  }

}