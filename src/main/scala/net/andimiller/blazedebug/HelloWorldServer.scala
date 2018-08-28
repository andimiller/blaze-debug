package net.andimiller.blazedebug

import cats.effect.{Effect, IO, ConcurrentEffect}
import fs2.StreamApp
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global

object HelloWorldServer extends StreamApp[IO] {

  def stream(args: List[String], requestShutdown: IO[Unit]) = ServerStream.stream[IO]
}

object ServerStream {

  def helloWorldService[F[_]: Effect] = new HelloWorldService[F].service

  def stream[F[_]: ConcurrentEffect](implicit ec: ExecutionContext) =
    BlazeBuilder[F]
      .bindHttp(8085, "0.0.0.0")
      .mountService(helloWorldService, "/")
      .serve
}
