package com.perhac.blog

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Span}
import org.scalatest.{LoneElement, Matchers, WordSpec}

abstract class BaseSpec extends WordSpec with ScalaFutures with Matchers with LoneElement {

  implicit val patience: PatienceConfig =
    PatienceConfig(
      timeout = scaled(Span(250, Millis)),
      interval = scaled(Span(20, Millis))
    )
}
