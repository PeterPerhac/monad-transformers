package com.perhac

import cats._
import cats.data._
import cats.implicits._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration.Inf
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends App {

  val program = for {
    x <- OptionT(Option(42).pure[Future])
  } yield x

  Await.result(program.getOrElse(0).map(println), Inf)

}

