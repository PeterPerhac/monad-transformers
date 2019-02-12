package com.perhac.blog

import cats.data._
import cats.implicits._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration.Inf
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  val program = OptionT.some[Future](42)

  Await.result(program.getOrElse(0).map(println), Inf)

}
