package com.perhac.blog

import cats.implicits._
import cats.data._

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration.Inf

object HelloTraverse extends App {

  import UserRepository._

  val magicFunction: Int => EitherT[Future, String, User] =
    id => findUserById(id).toRight(s"User id $id not found")

  val save: User => EitherT[Future, String, Unit] =
    user => {
      println(s"saving... $user")
      EitherT.cond[Future](user.name != "fail", (), s"Failed to save $user")
    }

  val names: List[User] => String =
    users => users.map(_.name).mkString(", ")

  val printIt: Future[_] => Unit =
    futureValue => {
      println(Await.result(futureValue, Inf))
      println("---")
    }

  /* ------------------------------ */

  println("traverse with OptionT")
  printIt(List(1, 2, 3, 4).traverse(findUserById).fold("Didn't find all")(names))

  println("traverse with EitherT")
  printIt(List(1, 2, 3, 4).traverse(magicFunction).map(names).merge)

  println("traverse and traverse_ with EitherT")
  printIt(List(1, 2, 3, 4).traverse(magicFunction).flatTap(_.traverse_(save)).map(names).merge)

  println("traverse with OptionT - fail")
  printIt(List(1, 2, 6, 3, 4).traverse(findUserById).fold("Didn't find all")(names))

  println("traverse with EitherT - fail")
  printIt(List(1, 2, 6, 3, 4).traverse(magicFunction).map(names).merge)

  println("traverse and traverse_ with EitherT")
  printIt(List(1, 2, 3, 4, 5).traverse(magicFunction).flatTap(_.traverse_(save)).map(names).merge)

}
