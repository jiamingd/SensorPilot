
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.actorRef2Scala


object SensorPilot extends App{
   val akkaSystem = ActorSystem("SensorPilotSystem")
	  val masterActor = akkaSystem.actorOf(Props[MasterActorMultipleGenerator],"MasterActorMulitpleGenerator")
  
	  
	  scala.io.StdIn.readLine("Hit ENTER to exit ...\n")
	  masterActor ! ShutDown
}