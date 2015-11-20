import akka.actor.Actor
import akka.actor.ActorRef
import scala.collection.immutable.HashMap
import scala.collection.mutable.LinkedList
import akka.actor.ActorLogging

class SensorRecepient extends Actor  with ActorLogging {
//  val tmp = new HashMap(); 
  var count = 0
  
//  comments: Can not use Stream[SensorMessage] solution here with AKKA
  var lastSensorMessage:SensorMessage= defaultNonetypeMsg
  
  def receive = {
    
    case sm:SensorMessage =>{
      log.info("SensorRecepient receiving new sensor signal:"+sm)
      log.info("From generator:"+sender.path.name)  //comments: for mulitiple generator scenario
      log.info("lastSensorMessage:"+lastSensorMessage)

      sm.sameSensorType(lastSensorMessage) match {
        case 1 =>  
          //comments: indicate current signal type is same in row with last signal received, 
          //recepient does not care what generator source is the signal( in multiple generator scenario )
          count+=1
          lastSensorMessage=sm
          log.debug("repeat count:"+count)
          sender ! BackoffMessage(count)
        case 0 =>
          //comments: different signal type from last one
          count=0;
          lastSensorMessage=sm
          log.info("We get different signal type, keep repeating count as :"+count)
      }  
    }
    
    case _ => log.info("recepient not understood sth")
  }

}