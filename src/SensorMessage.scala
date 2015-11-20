trait BaseMessage

case class BackoffMessage(seconds:Int) extends BaseMessage


object SendSensor extends  BaseMessage 
object ShutDown extends BaseMessage

sealed trait SensorMessage extends BaseMessage{
    def sameSensorType(sm:SensorMessage):Int
}

case object defaultNonetypeMsg extends SensorMessage{
  def sameSensorType(sm:SensorMessage):Int={100}
}
case class LightLevel(vale:Int) extends SensorMessage{
   def sameSensorType(sm:SensorMessage):Int={
     sm match {
       case c : LightLevel => 
         
         1
       case _ => 0
     }
   }
}
case class Temperature(value:Int) extends SensorMessage{
  def sameSensorType(sm:SensorMessage):Int={
     sm match {
       case c : Temperature => 
         
         1
       case _ => 0
     }
   }
}
case class MoistureLevel(value:Int) extends SensorMessage{
  def sameSensorType(sm:SensorMessage):Int={
     sm match {
       case c : MoistureLevel => 
         
         1
       case _ => 0
     }
   }
}



