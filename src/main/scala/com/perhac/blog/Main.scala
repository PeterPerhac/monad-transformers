package com.perhac.blog

import cats.implicits._

import scala.concurrent.Await.result
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration.Inf

object Main extends App {

  val separator = Future.successful("\n---\n")

  result(
    List(
      HelloOptionT("1"),
      HelloOptionT("2"),
      HelloOptionT("3"),
      HelloOptionT("4"),
      HelloOptionT("7"),
      HelloOptionT("something"),
      HelloOptionT(),
      HelloOptionT("1", "2", "3"),
      separator,
      HelloOptionT2("1"),
      HelloOptionT2("2"),
      HelloOptionT2("3"),
      HelloOptionT2("4"),
      HelloOptionT2("7"),
      HelloOptionT2("something"),
      HelloOptionT2(),
      HelloOptionT2("1", "2", "3"),
      separator,
      HelloFutureOptions("1"),
      HelloFutureOptions("2"),
      HelloFutureOptions("3"),
      HelloFutureOptions("4"),
      HelloFutureOptions("7"),
      HelloFutureOptions("something"),
      HelloFutureOptions(),
      HelloFutureOptions("1", "2", "3"),
      separator,
      HelloEitherT("1"),
      HelloEitherT("2"),
      HelloEitherT("3"),
      HelloEitherT("4"),
      HelloEitherT("7"),
      HelloEitherT("something"),
      HelloEitherT(),
      HelloEitherT("1", "2", "3")
    ).sequence,
    Inf
  ).foreach(println)

}
