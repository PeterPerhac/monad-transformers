package com.perhac.blog

class Tests extends BaseSpec {

  "my program working with Future Options" should passAllChecks(HelloFutureOptions.apply)
  "my program working with OptionTs" should passAllChecks(HelloOptionT.apply)
  "my program working with OptionTs (|>)" should passAllChecks(HelloOptionT2.apply)

  private def passAllChecks(program: MyProgram): Unit = {
    "find loyalty points balance for a user" in {
      program(Seq("1")).futureValue shouldBe "Peter, 33 with a balance of 5000 loyalty points."
    }

    "not find loyalty points balance" when {
      "user has not signed up for loyalty programme" in {
        program(Seq("3")).futureValue shouldBe "Loyalty profile for user ID 3 not found"
      }
      "non-existent user" in {
        program(Seq("10")).futureValue shouldBe "Loyalty profile for user ID 10 not found"
      }
      "the input is invalid" in {
        program(Seq("invalid")).futureValue shouldBe "Loyalty profile for user ID invalid not found"
      }
      "no input" in {
        program(Seq()).futureValue shouldBe "Loyalty profile for user ID <not specified> not found"
      }
    }
  }

}
