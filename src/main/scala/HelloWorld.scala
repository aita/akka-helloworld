import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import scala.util.{Failure, Success}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class HelloActor extends Actor {
  override def preStart(): Unit = println("first started")
  override def postStop(): Unit = println("first stopped")

  def receive: Receive = {
    case "hello" => sender ! "hello back at you"
    case _       => sender ! "huh?"
  }
}

object Main extends App {
  val system = ActorSystem("HelloSystem")
  // default Actor constructor
  val helloActor = system.actorOf(Props[HelloActor], name = "hello")
  implicit val timeout: Timeout = Timeout(5 seconds)
  val reply = helloActor ? "hello"
  reply.onComplete{
    case Success(msg: String) => println(msg)
    case Failure(_) => println("umm?")
  }
  system.terminate()
}
