

import akka.actor.ActorSystem
import akka.actor.Props
/*
object SingleSenderPilot extends App {

  val akkaSystem = ActorSystem("SensorPilotSystem")

  // actRef of both Receptor and router
  val singleRcptor = akkaSystem.actorOf(Props[SensorRecepient])
  val router = akkaSystem.actorOf(Props.empty.withRouter(RoundRobinRouter(routees = Vector[ActorRef](singleRcptor))))

  println("First Generator start sending")

  val generator1 = akkaSystem.actorOf(Props(new SensorGenerator(router, singleRcptor)), name = "generator1")

  scala.io.StdIn.readLine("Hit ENTER to exit ...\n")

  generator1 ! ShutDown

}
*/
object SingleSenderPilot extends App {
	  val akkaSystem = ActorSystem("SensorPilotSystem")
	  val masterActor = akkaSystem.actorOf(Props[MasterActor],"MasterActor")
  
	  
	  scala.io.StdIn.readLine("Hit ENTER to exit ...\n")
	  masterActor ! ShutDown
	  
}








