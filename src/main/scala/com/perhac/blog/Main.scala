package com.perhac.blog

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {

  println(Await.result(HelloOptionT("1"), Duration.Inf))
  println(Await.result(HelloOptionT("2"), Duration.Inf))
  println(Await.result(HelloOptionT("3"), Duration.Inf))
  println(Await.result(HelloOptionT("7"), Duration.Inf))
  println(Await.result(HelloOptionT("something"), Duration.Inf))
  println(Await.result(HelloOptionT(), Duration.Inf))
  println(Await.result(HelloOptionT("1", "2", "3"), Duration.Inf))

  println()
  println("---")
  println()

  println(Await.result(HelloOptionT2("1"), Duration.Inf))
  println(Await.result(HelloOptionT2("2"), Duration.Inf))
  println(Await.result(HelloOptionT2("3"), Duration.Inf))
  println(Await.result(HelloOptionT2("7"), Duration.Inf))
  println(Await.result(HelloOptionT2("something"), Duration.Inf))
  println(Await.result(HelloOptionT2(), Duration.Inf))
  println(Await.result(HelloOptionT2("1", "2", "3"), Duration.Inf))

  println()
  println("---")
  println()

  println(Await.result(HelloFutureOptions("1"), Duration.Inf))
  println(Await.result(HelloFutureOptions("2"), Duration.Inf))
  println(Await.result(HelloFutureOptions("3"), Duration.Inf))
  println(Await.result(HelloFutureOptions("7"), Duration.Inf))
  println(Await.result(HelloFutureOptions("something"), Duration.Inf))
  println(Await.result(HelloFutureOptions(), Duration.Inf))
  println(Await.result(HelloFutureOptions("1", "2", "3"), Duration.Inf))

}
