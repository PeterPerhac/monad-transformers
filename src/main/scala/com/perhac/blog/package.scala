package com.perhac

import cats.data.OptionT
import cats.instances.future._
import mouse.string._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

package object blog {

  type MyProgram = Seq[String] => Future[String]

  val stringToIntOption: String => Option[Int] = s => s.parseIntOption
  val stringToIntEither: String => Either[String, Int] = s =>
    s.parseInt.left.map(e => s"Failed to parse provided input: $e")

  object PixieDust {

    implicit class AnyEx[T](val v: T) extends AnyVal {
      def |>[U](f: T => U): U = f(v)
    }

    def lift[A](a: A): OptionT[Future, A] = OptionT.some(a)
    def liftFuture[A](f: Future[A]): OptionT[Future, A] = OptionT.liftF(f)
    def liftOption[A](o: Option[A]): OptionT[Future, A] = OptionT.fromOption(o)
    def liftFutureOption[A](f: Future[Option[A]]): OptionT[Future, A] = OptionT(f)

  }

}
