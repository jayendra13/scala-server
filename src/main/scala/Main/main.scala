package main.scala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model._
//import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn

object  Main{

  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()

    implicit val executionContext = system.dispatcher

    val requestHandler: HttpRequest => HttpResponse = {

      case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
        HttpResponse(entity = HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          "<html><body>Hello World</body></html>"
        ))

      case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
        HttpResponse(entity = "PONG!")

      case HttpRequest(GET, Uri.Path("/crash"), _, _, _) =>
        sys.error("BOOM!")

      case r: HttpRequest =>
        r.discardEntityBytes()
        HttpResponse(404, entity = "Unknown resource!")
    }

//    val route =
//      path("hello") {
//        get {
//          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
//        }
//      }

    val bindingFuture = Http().bindAndHandleSync(requestHandler, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate())
  }
}