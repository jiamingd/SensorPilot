import akka.actor.Actor
import akka.routing.RoundRobinRouter
import akka.actor.Props
import akka.actor.ActorRef
import akka.actor.Stash
import akka.actor.Cancellable
import scala.concurrent.duration.FiniteDuration
import scala.util.Random
import java.text.DecimalFormat
import akka.actor.PoisonPill
import akka.event.LoggingAdapter
import akka.event.Logging
import akka.actor.ActorLogging

class SensorGenerator( router : ActorRef,     recepient : ActorRef) extends Actor with ActorLogging{
  import context._

  private var scheduler: Cancellable = _
  val sensorTypeCount:Int = 3
  
  //comments: to guarantee either this / dispathes from BackoffMessage could safely  update/read
  @volatile var lapsecounter =0
  
  
  override def preStart(): Unit = {
		  log.info("Enable scheduler on pre-start")
	import scala.concurrent.duration._
    scheduler = context.system.scheduler.schedule(
      initialDelay = 1 seconds,
      interval = 1 seconds,
      receiver = self,
      message = SendSensor
    )
  }  

  private def createRandomSensorMessage():SensorMessage={
    
    val value = Random.nextInt(Integer.MAX_VALUE)%1000  //commments: Just for case demo, no intention for covering real case data type/range
    
    val typeInt = Random.nextInt(Integer.MAX_VALUE) % sensorTypeCount
    	  
    	  typeInt match{
    	    case 0 => LightLevel(value)  
    	    case 1 => Temperature(value)
    	    case 2 => MoistureLevel(value)
    	    case _ =>  LightLevel(value) //commnets:still provide default one although no need
    	  }
    
  }
  
  def receive = {
    
    case SendSensor =>
      log.info(  "sleepcounter:"+lapsecounter)
      log.info("Generator: SendSensor message received:")
    
      lapsecounter match {
        case 0 =>
        log.info("Ok to send signal after checing no repeated signal type"+lapsecounter)
        router ! createRandomSensorMessage
    	  
        case _ =>
          log.info("STOP susppend temporarily by requiring time lapse of secode/s:"+lapsecounter)
          lapsecounter-=1
        
      }
     
    case boff : BackoffMessage =>
      log.info("Generator: BackoffMessage message received, requested to hold sending signal with second lapse of:"+boff.seconds)
      lapsecounter = boff.seconds 

    case _ => log.info("nothing understood by generator")
      
    
  }

}